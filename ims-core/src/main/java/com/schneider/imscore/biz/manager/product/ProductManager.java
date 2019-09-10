package com.schneider.imscore.biz.manager.product;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSSClient;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.green.model.v20180509.ImageSyncScanRequest;
import com.aliyuncs.http.FormatType;
import com.aliyuncs.http.HttpResponse;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.imagesearch.model.v20190325.AddImageRequest;
import com.aliyuncs.imagesearch.model.v20190325.AddImageResponse;
import com.aliyuncs.imagesearch.model.v20190325.SearchImageRequest;
import com.aliyuncs.imagesearch.model.v20190325.SearchImageResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.schneider.imscore.enums.LanguageEnum;
import com.schneider.imscore.mapper.product.ImageSearchAddMapper;
import com.schneider.imscore.mapper.product.ProductSkuMapper;
import com.schneider.imscore.po.product.ImageSearchAddPO;
import com.schneider.imscore.po.product.ProductSkuPO;
import com.schneider.imscore.resp.ResultCode;
import com.schneider.imscore.resp.exception.BizException;
import com.schneider.imscore.util.AliyunOSSClientUtil;
import com.schneider.imscore.util.DateUtils;
import com.schneider.imscore.util.FileUtil;
import com.schneider.imscore.util.StringOperationUtil;
import com.schneider.imscore.vo.product.ProductVO;
import com.schneider.imscore.vo.product.req.ProductReqData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.schneider.imscore.constant.Constant.IMAGE_SEARCH_RESULT_LIMIT;


/**
 * @author liyuan
 * @createDate 2019/08/22 10:05
 * @Description 产品manager
 */
@Slf4j
@Component
public class ProductManager {

    @Value("${aliyun.image.region}")
    private String region;

    @Value("${aliyun.image.endpoint}")
    private String endpoint;

    @Value("${aliyun.image.accessKeyId}")
    private String accessKeyId;

    @Value("${aliyun.image.accessKeySecret}")
    private String accessKeySecret;

    @Value("${aliyun.image.instanceName}")
    private String instanceName;

    @Value("${aliyun.green.endpoint}")
    private String greenEndpoint;

    @Autowired
    private AliyunOSSClientUtil aliyunOSSClientUtil;

    @Autowired
    private ProductSkuMapper productSkuMapper;

    @Autowired
    private ImageSearchAddMapper imageSearchAddMapper;


    /**
     * 图片搜索
     * @param multipartFile
     * @return
     * @throws BizException
     * @throws ClientException
     */
    public List<ProductVO> listProductsBySearch(MultipartFile multipartFile,String language) throws BizException {
        List<ProductVO> productVOS = new ArrayList<>();
       // 1.图片ocr
        List<String> textOcr = imageOcr(multipartFile);
        log.info("ocr返回结果{}",textOcr.toString());
        // 过滤长度大于等于7的
        List<String> ocr = textOcr.stream().filter(item -> item.length()>= 7).collect(Collectors.toList());
        // 过滤特殊字符
        List<String> text = StringOperationUtil.replaceSpecialChar(ocr);
        // 2.图片搜索
        SearchImageResponse response = imageSearch(multipartFile);
        if (response != null){
            List<SearchImageResponse.Auction> auctions = response.getAuctions();
            if (!CollectionUtils.isEmpty(auctions)){
                if (!CollectionUtils.isEmpty(text)){
                    // ocr的结果去数据库查询 查到：合并   查不到：返回图搜
                    List<ProductSkuPO> productSkuPOs = new ArrayList<>();
                     productSkuPOs = productSkuMapper.listProductsBySku(text);
                    List<String> productIds = new ArrayList<>();
                    if (CollectionUtils.isEmpty(productSkuPOs)){
                        for (String str: text) {
                            if (str.contains("O")){
                                String o = str.replaceAll("O", "0");
                                productIds.add(o);
                            }
                        }
                        if (!CollectionUtils.isEmpty(productIds)){
                            productSkuPOs = productSkuMapper.listProductsBySku(productIds);
                        }
                    }
                    // ocr在库中没有查到 则模糊查询
                    if (CollectionUtils.isEmpty(productSkuPOs)){
                        productSkuPOs = productSkuMapper.listProductsLikeSku(text);
                        if (productSkuPOs .size() > 1){
                            productSkuPOs = null;
                        }
                    }

                    // ocr 结果未查到
                    if (CollectionUtils.isEmpty(productSkuPOs)){
                        productVOS = productVOList(auctions, productVOS,IMAGE_SEARCH_RESULT_LIMIT,language);
                    }else {
                        // ocr结果查到了
                        productVOS = productVOList(auctions, productVOS,4,language);
                        ProductVO productVO = new ProductVO();
                        ProductSkuPO productOcrPO = productSkuPOs.get(0);
                        // 初始化
                        init(productVO,productOcrPO,language);

                        OSSClient ossClient = aliyunOSSClientUtil.getOSSClient();
                        String url = aliyunOSSClientUtil.getUrlByFileKey(ossClient, productOcrPO.getOssKey());
                        productVO.setUrl(url);

                        productVOS.add(productVO);
                        productVOS = productVOS.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getProductId()))), ArrayList::new));

                        Collections.swap(productVOS, productVOS.size()-1, 0);
                    }
                }else {
                    productVOS = productVOList(auctions, productVOS,IMAGE_SEARCH_RESULT_LIMIT,language);
                }
            }
        }
        // 3.返回搜索结果列表
        return productVOS;
    }

    /**
     * 多语言赋值
     * @param productVO
     * @param productOcrPO
     */
    private void init(ProductVO productVO ,ProductSkuPO productOcrPO,String language){
        productVO.setProductId(productOcrPO.getReference());
        if (StringUtils.isEmpty(language) || LanguageEnum.LANGUAGE_ENGLISH.getKey().equals(language)){
            productVO.setBrand(productOcrPO.getBrand());
            productVO.setCategory(productOcrPO.getCategory());
            productVO.setDescription(productOcrPO.getDescription());
            productVO.setFamily(productOcrPO.getFamily());
        }else if (LanguageEnum.LANGUAGE_CHINESE.getKey().equals(language)){
            productVO.setBrand(productOcrPO.getBrandChinese());
            productVO.setCategory(productOcrPO.getCategoryChinese());
            productVO.setDescription(productOcrPO.getDescriptionChinese());
            productVO.setFamily(productOcrPO.getFamilyChinese());
        }else if (LanguageEnum.LANGUAGE_PORTUGUESE.getKey().equals(language)){
            productVO.setBrand(productOcrPO.getBrandPortuguese());
            productVO.setCategory(productOcrPO.getCategoryPortuguese());
            productVO.setDescription(productOcrPO.getDescriptionPortuguese());
            productVO.setFamily(productOcrPO.getFamilyPortuguese());
        }else if (LanguageEnum.LANGUAGE_RUSSIAN.getKey().equals(language)){
            productVO.setBrand(productOcrPO.getBrandRussian());
            productVO.setCategory(productOcrPO.getCategoryRussian());
            productVO.setDescription(productOcrPO.getDescriptionRussian());
            productVO.setFamily(productOcrPO.getFamilyRussian());
        }
    }


    /**
     * 直接返回图搜结果集（5条）
     * @param auctionsList
     * @param productVOS
     * @return
     */
    private List<ProductVO> productVOList(List<SearchImageResponse.Auction> auctionsList,List<ProductVO> productVOS,int limit,String language){
            List<SearchImageResponse.Auction> auctions = new ArrayList<>();
            if (auctionsList.size() > limit){
                auctions = auctionsList.subList(0, limit);
                productVOS = convert(auctions,language);
            }else {
                productVOS = convert(auctionsList,language);
            }
        return productVOS;
    }

    /**
     * 转换
     * @param auctions
     * @return
     */
    private  List<ProductVO> convert(List<SearchImageResponse.Auction> auctions,String language){
        OSSClient ossClient = aliyunOSSClientUtil.getOSSClient();
        List<ProductVO> productVOS = new ArrayList<>();
        for (SearchImageResponse.Auction auction: auctions) {
            ProductVO productVO = new ProductVO();
            productVO.setProductId(auction.getProductId());
            String customContent = auction.getCustomContent();
            if (!StringUtils.isEmpty(customContent)){
                // customContent 为json字符串
                ProductVO productVO1 = null;
                try {
                    productVO1 = JSON.parseObject(customContent, ProductVO.class);
                } catch (Exception e) {
                    log.error("customContent json结果解析失败customContent:{}",customContent,e);
                    continue;
                }
                if (productVO1 != null){
                    String url = aliyunOSSClientUtil.getUrlByFileKey(ossClient, productVO1.getKey());
                    if (StringUtils.isEmpty(language) || LanguageEnum.LANGUAGE_ENGLISH.getKey().equals(language)){
                        productVO.setBrand(productVO1.getBrand());
                        productVO.setCategory(productVO1.getCategory());
                        productVO.setDescription(productVO1.getDescription());
                        productVO.setFamily(productVO1.getFamily());
                    }else if (LanguageEnum.LANGUAGE_CHINESE.getKey().equals(language)){
                        productVO.setBrand(productVO1.getBrandChinese());
                        productVO.setCategory(productVO1.getCategoryChinese());
                        productVO.setDescription(productVO1.getDescriptionChinese());
                        productVO.setFamily(productVO1.getFamilyChinese());
                    }else if (LanguageEnum.LANGUAGE_PORTUGUESE.getKey().equals(language)){
                        productVO.setBrand(productVO1.getBrandPortuguese());
                        productVO.setCategory(productVO1.getCategoryPortuguese());
                        productVO.setDescription(productVO1.getDescriptionPortuguese());
                        productVO.setFamily(productVO1.getFamilyPortuguese());
                    }else if (LanguageEnum.LANGUAGE_RUSSIAN.getKey().equals(language)){
                        productVO.setBrand(productVO1.getBrandRussian());
                        productVO.setCategory(productVO1.getCategoryRussian());
                        productVO.setDescription(productVO1.getDescriptionRussian());
                        productVO.setFamily(productVO1.getFamilyRussian());
                    }
                    productVO.setUrl(url);
                    productVOS.add(productVO);
                }
            }
        }
        return productVOS;
    }



    /**
     * 图像搜索
     * @param multipartFile
     * @return
     * @throws Exception
     */
    private SearchImageResponse imageSearch(MultipartFile multipartFile) throws BizException{
        DefaultProfile.addEndpoint(region, "ImageSearch", endpoint);
        IClientProfile profile = DefaultProfile.getProfile(region, accessKeyId, accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);

        SearchImageRequest request = new SearchImageRequest();
        // 必填，图像搜索实例名称。
        request.setInstanceName(instanceName);
        // 根据图片搜索相似图片。
        request.setType("SearchByPic");

        // 图片的base64编码
        String encodePicContent = FileUtil.multipartFileToBase64(multipartFile);
        request.setPicContent(encodePicContent);
        // 选填，商品类目。
        request.setCategoryId(88888888);
        // 主体识别
        request.setCrop(true);

        // 选填，返回结果的数目。取值范围：1-100。默认值：10。
        request.setNum(20);

        SearchImageResponse response = null;
        try {
            response = client.getAcsResponse(request);
        } catch (ClientException e) {
            log.error("阿里云图像搜索接口调用失败,入参SearchImageRequest{}",JSON.toJSONString(request),e);
           throw new BizException(ResultCode.IMAGE_SEARCH_ERROR.getCode(),e.getErrCode());
        }
        return response;
    }

    /**
     * ocr 参数初始化
     * @return
     */
    private  ImageSyncScanRequest imageOcrInit(MultipartFile multipartFile) throws BizException{
        ImageSyncScanRequest imageSyncScanRequest = new ImageSyncScanRequest();
        imageSyncScanRequest.setAcceptFormat(FormatType.JSON);
        imageSyncScanRequest.setMethod(MethodType.POST);
        imageSyncScanRequest.setEncoding("utf-8");
        imageSyncScanRequest.setProtocol(ProtocolType.HTTP);

        JSONObject httpBody = new JSONObject();
        // ocr
        httpBody.put("scenes", Arrays.asList("ocr"));

        String url = null;
        try {
            OSSClient ossClient = aliyunOSSClientUtil.getOSSClient();
            String dateStr = DateUtils.format(Calendar.getInstance().getTime(),
                    "yyyyMMddHHmmssSSSS");
            String key = MessageFormat.format("ocr/{0}{1}", dateStr + "/", multipartFile.getOriginalFilename());
            url = aliyunOSSClientUtil.uploadFile(ossClient, multipartFile.getInputStream(), key);
        } catch (IOException e) {
            throw new  BizException(ResultCode.OSS_UPLOAD_ERROR);
        }

        JSONObject task = new JSONObject();
        task.put("dataId", UUID.randomUUID().toString());

        //设置图片链接为上传后的url
        task.put("url", url);
        task.put("time", new Date());
        httpBody.put("tasks", Arrays.asList(task));

        imageSyncScanRequest.setHttpContent(org.apache.commons.codec.binary.StringUtils.getBytesUtf8(httpBody.toJSONString()), "UTF-8", FormatType.JSON);

        /**
         * 请设置超时时间, 服务端全链路处理超时时间为10秒，请做相应设置
         * 如果您设置的ReadTimeout 小于服务端处理的时间，程序中会获得一个read timeout 异常
         */
        imageSyncScanRequest.setConnectTimeout(3000);
        imageSyncScanRequest.setReadTimeout(10000);
        return imageSyncScanRequest;
    }


    /**
     * 图片ocr
     * @param multipartFile
     * @throws Exception
     */
    private List<String> imageOcr(MultipartFile multipartFile) throws BizException{
        IClientProfile profiles = DefaultProfile.getProfile(region, accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint(region, "Green", greenEndpoint);
        IAcsClient client = new DefaultAcsClient(profiles);

        // 参数初始化
        ImageSyncScanRequest imageSyncScanRequest = imageOcrInit(multipartFile);

        HttpResponse httpResponse = null;

        try {
            httpResponse = client.doAction(imageSyncScanRequest);
        } catch (ClientException e) {
            log.error("阿里云ocr调用失败 入参imageSyncScanRequest：{}",JSON.toJSONString(imageSyncScanRequest),e);
            throw new BizException(ResultCode.OCR_ERROR);
        }

        //服务端接收到请求，并完成处理返回的结果
        return ocrResponse(httpResponse);
    }

    /**
     * ocr结果解析
     * @param httpResponse
     * @return
     * @throws BizException
     */
    private List<String> ocrResponse(HttpResponse httpResponse) throws BizException{
        List<String> ocrResults = new ArrayList<>();
        if (httpResponse != null && httpResponse.isSuccess()) {
            JSONObject scrResponse = JSON.parseObject(org.apache.commons.codec.binary.StringUtils.newStringUtf8(httpResponse.getHttpContent()));
            int requestCode = scrResponse.getIntValue("code");
            //每一张图片的检测结果
            JSONArray taskResults = scrResponse.getJSONArray("data");
            if (200 == requestCode) {
                for (Object taskResult : taskResults) {
                    //单张图片的处理结果
                    int taskCode = ((JSONObject) taskResult).getIntValue("code");
                    //图片要检测的场景的处理结果, 如果是多个场景，则会有每个场景的结果
                    JSONArray sceneResults = ((JSONObject) taskResult).getJSONArray("results");
                    if (200 == taskCode) {
                        for (Object sceneResult : sceneResults) {
                            String scene = ((JSONObject) sceneResult).getString("scene");
                            String suggestion = ((JSONObject) sceneResult).getString("suggestion");
                            if("review" .equals(suggestion) && "ocr".equals(scene)){
                                JSONArray ocrLocations = ((JSONObject) sceneResult).getJSONArray("ocrLocations");
                                if (ocrLocations !=null){
                                    for (Object ocrLocation:ocrLocations ) {
                                        // 识别出的信息
                                        String text = ((JSONObject) ocrLocation).getString("text");
                                        ocrResults.add(text);
                                    }
                                }
                            }
                        }
                    } else {
                        //单张图片处理失败, 原因是具体的情况详细分析
                        log.error("ocr检测图片单张图片处理失败 task response:{}",JSON.toJSONString(taskResult));
                        throw new BizException(ResultCode.SINGLE_IMAGE_CHECK_ERROR);
                    }
                }
            } else {
                /**
                 * 表明请求整体处理失败，原因视具体的情况详细分析
                 */
                log.error("ocr:the whole image scan request failed. response:{}",JSON.toJSONString(scrResponse));
                throw new BizException(ResultCode.WHOLE_IMAGE_CHECK_ERROR);
            }
        }else {
            log.error("ocr:the whole image scan request failed. httpResponse:{}",JSON.toJSONString(httpResponse));
            throw new BizException(ResultCode.WHOLE_IMAGE_CHECK_ERROR);
        }
        return ocrResults;
    }

    /**
     * 新增图搜
     * @param productReqData
     * @param multipartFile
     * @return
     * @throws ClientException
     */
    @Transactional(rollbackFor = {Exception.class, BizException.class})
    public AddImageResponse saveImageSearch(ProductReqData productReqData,MultipartFile multipartFile)throws BizException {
        DefaultProfile.addEndpoint(region, "ImageSearch", endpoint);
        IClientProfile profile = DefaultProfile.getProfile(region, accessKeyId, accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);

        AddImageRequest request = new AddImageRequest();
        // 必填，图像搜索实例名称。
        request.setInstanceName(instanceName);
        // 必填，商品id，最多支持 512个字符。
        request.setProductId(productReqData.getProductId());

        // 2. 如果多次添加图片具有相同的ProductId + PicName，以最后一次添加为准，前面添加的的图片将被覆盖。
        request.setPicName(multipartFile.getOriginalFilename());
        // 2. 对于通用搜索：不论是否设置类目，系统会将类目设置为88888888。
        request.setCategoryId(88888888);
        // 图片的base64编码
        String encodePicContent = FileUtil.multipartFileToBase64(multipartFile);
        request.setPicContent(encodePicContent);
        // 必填，图片内容，Base64编码。
        request.setPicContent(encodePicContent);

        // 选填，用户自定义的内容，最多支持 4096个字符。

        ProductSkuPO productSkuPO = new ProductSkuPO();
        productSkuPO.setBrand(productReqData.getBrand());
        productSkuPO.setDescription(productReqData.getDescription());
        productSkuPO.setCategory(productReqData.getCategory());
        productSkuPO.setFamily(productReqData.getFamily());

        request.setCustomContent(JSON.toJSONString(productSkuPO));
        AddImageResponse acsResponse = null;
        try {
            acsResponse = client.getAcsResponse(request);
        } catch (ClientException e) {
            log.error("调用阿里云新增图搜图片api失败,入参productReqData:{}",JSON.toJSONString(request),e);
            throw new BizException(ResultCode.IMAGE_SEARCH_ADD_ERROR.getCode(),e.getErrCode());
        }

        ImageSearchAddPO imageSearchAddPO = new ImageSearchAddPO();
        imageSearchAddPO.setRequestId(acsResponse.getRequestId());
        imageSearchAddPO.setImageName(multipartFile.getOriginalFilename());
        imageSearchAddPO.setCustomContent(request.getCustomContent());
        imageSearchAddPO.setProductId(productReqData.getProductId());
        imageSearchAddPO.setResult(JSON.toJSONString(acsResponse));
        imageSearchAddPO.setCategoryId(request.getCategoryId().toString());
        imageSearchAddPO.setInstanceName(instanceName);
        imageSearchAddPO.setCreated(new Date());
        imageSearchAddPO.setModified(new Date());
        imageSearchAddMapper.saveImageSearch(imageSearchAddPO);
        return  acsResponse;
    }



}
