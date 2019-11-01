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
import com.schneider.imscore.mapper.product.ImageSearchLogMapper;
import com.schneider.imscore.mapper.product.ProductSkuMapper;
import com.schneider.imscore.mapper.product.SkuMatchingMapper;
import com.schneider.imscore.po.product.ImageSearchAddPO;
import com.schneider.imscore.po.product.ImageSearchLogPO;
import com.schneider.imscore.po.product.ProductSkuPO;
import com.schneider.imscore.po.product.SkuMatchingPO;
import com.schneider.imscore.resp.ResultCode;
import com.schneider.imscore.resp.exception.BizException;
import com.schneider.imscore.util.*;
import com.schneider.imscore.vo.product.Product;
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
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;
import static com.schneider.imscore.constant.Constant.IMAGE_SEARCH_RESULT_LIMIT;
import static com.schneider.imscore.constant.Constant.SUCCESS_CODE;
import static java.util.stream.Collectors.toList;


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

    @Autowired
    private ImageSearchLogMapper imageSearchLogMapper;

    @Autowired
    private SkuMatchingMapper skuMatchingMapper;

    /**
     * 图片搜索
     * @param language
     * @param multipartFile
     * @return
     * @throws BizException
     */
    public List<ProductVO> listProductsBySearch(MultipartFile multipartFile, String language, HttpServletRequest request,Long ocrStart) throws BizException {
        // 图片ocr
        List<String> text = imageOcr(multipartFile);
        long ocrEnd = System.currentTimeMillis();
        int ocrTime = (int) (ocrEnd - ocrStart);
        String jsonOcr = JSON.toJSONString(text);
        log.info("ocr返回结果{}",jsonOcr);

        // 获取ip地址
        String ipAddress = IpUtil.getIpAddress(request);
        String country = Geoip2Util.getCountryByIp(ipAddress);
        // 图片压缩
        MultipartFile[] multipartFiles = new MultipartFile[1];
        multipartFiles[0] = multipartFile;
        MultipartFile[] result = ImageSizeUtil.byte2Base64StringFun(multipartFiles);
        multipartFile = result[0];

        long imageSearchStart = System.currentTimeMillis();
        // 图搜
        SearchImageResponse response = imageSearch(multipartFile);
        String searchImageResponse = JSON.toJSONString(response);

        long imageSearchEnd = System.currentTimeMillis();
        int imageSearchTime = (int)(imageSearchEnd - imageSearchStart);

        // 图搜和ocr结果解析
        List<ProductVO> productVOS = responseResult(response, text, language);

        // 接口整体调用时长
        long endTime = System.currentTimeMillis();
        int wholeTime = (int) (endTime - ocrStart);
        // 监控日志
        saveImageSearchLog(ocrTime,imageSearchTime,ipAddress,jsonOcr,wholeTime,country,searchImageResponse);
        return  productVOS;
    }

    /**
     * 记录接口调用情况
     * @param ocrTime
     * @param imageSearchTime
     * @param ipAddress
     * @param ocrResult
     * @param wholeApiTime
     */
    private void saveImageSearchLog(int ocrTime,int imageSearchTime,String ipAddress,String ocrResult,int wholeApiTime,
                                    String country,String searchImageResponse){
        ImageSearchLogPO imageSearchLogPO = new ImageSearchLogPO();
        imageSearchLogPO.setIpAddress(ipAddress);
        imageSearchLogPO.setOcrTime(ocrTime);
        imageSearchLogPO.setImageSearchTime(imageSearchTime);
        imageSearchLogPO.setWholeApiTime(wholeApiTime);
        imageSearchLogPO.setOcrResult(ocrResult);
        imageSearchLogPO.setCreated(new Date());
        imageSearchLogPO.setCountry(country);
        imageSearchLogPO.setImageSearchResult(searchImageResponse);
        // todo
//        imageSearchLogMapper.saveImageSearchLog(imageSearchLogPO);
    }



    /**
     * 结果解析
     * @param response
     * @param text
     * @param language
     * @return
     */
    private List<ProductVO> responseResult(SearchImageResponse response, List<String> text,String language){
        List<ProductVO> productVOS = new ArrayList<>();
        if (response != null){
            List<SearchImageResponse.Auction> auctions = response.getAuctions();
            if (!CollectionUtils.isEmpty(auctions)){
                if (!CollectionUtils.isEmpty(text)){
                    // ocr的结果去数据库查询 查到：合并   查不到：返回图搜
                    List<ProductSkuPO>  productSkuPOs = productSkuMapper.listProductsBySku(text);
                    if (CollectionUtils.isEmpty(productSkuPOs)){
                        // O替换成0
                        List<String> productIds = doSpecialOcr(text);
                        if (!CollectionUtils.isEmpty(productIds)){
                            // ocr结果数据库中精准匹配
                            productSkuPOs = productSkuMapper.listProductsBySku(productIds);
                        }
                    }
                    // ocr在库中没有查到 则模糊查询
                    if (CollectionUtils.isEmpty(productSkuPOs)){
                        List<String> ocr = text.stream().filter(item -> item.length()>= 7).collect(Collectors.toList());
                        if (!CollectionUtils.isEmpty(ocr)){
                            productSkuPOs = productSkuMapper.listProductsLikeSku(ocr);
                            if (productSkuPOs .size() > 1){
                                productSkuPOs = new ArrayList<>();
                            }
                        }
                    }
                    // 查询出来的ocr结果逐一去数据库中description字段模糊 直到查询到数据
//                    productSkuPOs = fuzzySearch(productSkuPOs, text);

                    // ocr 结果未查到
                    if (CollectionUtils.isEmpty(productSkuPOs)){
                        List<ProductVO> productVOS1 = imageSearchOcrFilter(auctions, text, language);
                        productVOS.addAll(productVOS1);
                        productVOS = removeDuplicateContain(productVOS);
                    }else {
                        int size = productSkuPOs.size();
                        OSSClient ossClient = aliyunOSSClientUtil.getOSSClient();
                            for (int i = 0; i < size; i++) {
                                ProductSkuPO productSkuPO = productSkuPOs.get(i);
                                ProductVO productVO = new ProductVO();
                                init(productVO,productSkuPO,language,ossClient);
                                productVOS.add(productVO);
                            }
                            List<ProductVO> vos = productVOList(auctions, productVOS, IMAGE_SEARCH_RESULT_LIMIT - size , language);
                            productVOS.addAll(vos);
                            productVOS = removeDuplicateContain(productVOS);
                    }
                }else {
                    productVOS = productVOList(auctions, productVOS,IMAGE_SEARCH_RESULT_LIMIT,language);
                }
            }
        }
        return productVOS;
    }

    /**
     * ocr结果含有O替换成0
     * @param text
     * @return
     */
    private List<String> doSpecialOcr(List<String> text){
        List<String> productIds = new ArrayList<>();
        for (String str: text) {
            if (str.contains("O")){
                String o = str.replaceAll("O", "0");
                productIds.add(o);
            }
        }
        return productIds;
    }


    /**
     * 查询出来的ocr结果逐一去数据库中description字段模糊查询 直到查询到数据
     * @param productSkuPOs
     * @param text
     * @return
     */
    private List<ProductSkuPO> fuzzySearch(List<ProductSkuPO>  productSkuPOs,List<String> text){
        if (CollectionUtils.isEmpty(productSkuPOs)){
            boolean flag = false;
            int number = 0;
            for (int i = 0; i < text.size(); i++) {
                String str = text.get(i);
                if (str.length() > 2){
                    // 通过ocr模糊查询
                    productSkuPOs = productSkuMapper.selectProductLikeDescription(str);
                    if (!CollectionUtils.isEmpty(productSkuPOs) && productSkuPOs.size() < 500 && productSkuPOs.size()> 10){
                        flag = true;
                        number = i;
                        break;
                    }
                }
            }
            // 以数据库查到的数据为基准，过滤含有ocr的结果
            if (flag){
                for (int i = number +1; i < text.size(); i++) {
                    String firstOcr = text.get(i);
                    List<ProductSkuPO> collect = productSkuPOs.stream().filter(item -> item.getDescriptionOcr().contains(firstOcr)).collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(collect)){
                        productSkuPOs = collect;
                    }
                }
            }else {
                // 模糊查询说明没有符合结果
                return new ArrayList<>();
            }
        }
        return productSkuPOs;
    }

    /**
     * 图搜库从description中过滤ocr
     * @param auctions
     * @param text
     * @param language
     * @return
     */
    private  List<ProductVO>  imageSearchOcrFilter(List<SearchImageResponse.Auction> auctions,List<String> text,String language){
        OSSClient ossClient = aliyunOSSClientUtil.getOSSClient();

        List<ProductVO> productVOS = new ArrayList<>();
        for (SearchImageResponse.Auction auction: auctions) {
            String customContent = auction.getCustomContent();
            ProductVO productVO = JSON.parseObject(customContent, ProductVO.class);
            productVO.setProductId(auction.getProductId());
            productVO.setDescriptionOcr(productVO.getDescription().replaceAll(" ",""));
            String url = aliyunOSSClientUtil.getUrlByFileKey(ossClient, productVO.getKey());
            productVO.setUrl(url);
            if (StringUtils.isEmpty(language) || LanguageEnum.LANGUAGE_ENGLISH.getKey().equals(language)){
                String description = doSpecialDescription(productVO.getBrand(), productVO.getFamily(),
                        productVO.getCategory(), productVO.getDescription());
                productVO.setDescription(description);

            }else if (LanguageEnum.LANGUAGE_CHINESE.getKey().equals(language)){
                productVO.setBrand(productVO.getBrandChinese());
                productVO.setCategory(productVO.getCategoryChinese());
                String description = doSpecialDescription(productVO.getBrandChinese(), productVO.getFamilyChinese(),
                        productVO.getCategoryChinese(), productVO.getDescriptionChinese());
                productVO.setDescription(description);
                productVO.setFamily(productVO.getFamilyChinese());
            }else if (LanguageEnum.LANGUAGE_PORTUGUESE.getKey().equals(language)){
                productVO.setBrand(productVO.getBrandPortuguese());
                productVO.setCategory(productVO.getCategoryPortuguese());
                String description = doSpecialDescription(productVO.getBrandPortuguese(), productVO.getFamilyPortuguese(),
                        productVO.getCategoryPortuguese(), productVO.getDescriptionPortuguese());
                productVO.setFamily(productVO.getFamilyPortuguese());
                productVO.setDescription(description);
            }else if (LanguageEnum.LANGUAGE_RUSSIAN.getKey().equals(language)){
                productVO.setBrand(productVO.getBrandRussian());
                productVO.setCategory(productVO.getCategoryRussian());
                String description = doSpecialDescription(productVO.getBrandRussian(), productVO.getFamilyRussian(),
                        productVO.getCategoryRussian(), productVO.getDescriptionRussian());
                productVO.setFamily(productVO.getFamilyRussian());
                productVO.setDescription(description);
            }
            productVOS.add(productVO);
        }
        for (int i = 0; i < text.size(); i++) {
            String ocr = text.get(i);
            // 超过三个字符的进行过滤
            if (ocr.length() >= 3){
                List<ProductVO> collect = productVOS.stream().filter(item -> item.getDescriptionOcr().contains(ocr)).collect(toList());
                if (!CollectionUtils.isEmpty(collect)){
                    productVOS = collect;
                }
            }
        }

        // 过滤后的结果集
        int size = productVOS.size();
        // 过滤无结果
        if (size == 50){
            return productVOList(auctions, productVOS, 5, language);
        }
        if (size < 5 && size >0 ){
            List<ProductVO> productVOList = productVOList(auctions, productVOS, 5 - size, language);
            productVOS.addAll(productVOList);
        }else if (size > 5){
            productVOS = productVOS.subList(0, 5);
        }
        return productVOS;
    }



    /**
     * 利用list.contain() 去重
     * @param list
     * @return
     */
    private   <T> List<T> removeDuplicateContain(List<T> list){
        List<T> listTemp = new ArrayList<>();
        for (T aList : list) {
            if (!listTemp.contains(aList)) {
                listTemp.add(aList);
            }
        }
        return listTemp;
    }


    /**
     * 说明中去除brand family category
     * @param brand
     * @param family
     * @param category
     * @param description
     * @return
     */
    public String doSpecialDescription(String brand ,String family, String category,String description){
        if (!StringUtils.isEmpty(brand)&&
        !StringUtils.isEmpty(family)&&
        !StringUtils.isEmpty(category)&&
        !StringUtils.isEmpty(description)){
            return description.replaceAll(brand+ " ", "").replaceAll(family+ " ", "").replaceAll(category+ " ", "");
        }else {
            return description;
        }
    }


    /**
     * 多语言赋值
     * @param productVO
     * @param productOcrPO
     * @param language
     * @param ossClient
     */
    private void init(ProductVO productVO ,ProductSkuPO productOcrPO,String language,OSSClient ossClient){
        productVO.setProductId(productOcrPO.getReference());
        if (StringUtils.isEmpty(language) || LanguageEnum.LANGUAGE_ENGLISH.getKey().equals(language)){
            productVO.setBrand(productOcrPO.getBrand());
            productVO.setCategory(productOcrPO.getCategory());
            String description = doSpecialDescription(productOcrPO.getBrand(), productOcrPO.getFamily(),
                    productOcrPO.getCategory(), productOcrPO.getDescription());
            productVO.setDescription(description);
            productVO.setFamily(productOcrPO.getFamily());
        }else if (LanguageEnum.LANGUAGE_CHINESE.getKey().equals(language)){
            productVO.setBrand(productOcrPO.getBrandChinese());
            productVO.setCategory(productOcrPO.getCategoryChinese());
            String description = doSpecialDescription(productOcrPO.getBrandChinese(), productOcrPO.getFamilyChinese(),
                    productOcrPO.getCategoryChinese(), productOcrPO.getDescriptionChinese());
            productVO.setDescription(description);
            productVO.setFamily(productOcrPO.getFamilyChinese());
        }else if (LanguageEnum.LANGUAGE_PORTUGUESE.getKey().equals(language)){
            productVO.setBrand(productOcrPO.getBrandPortuguese());
            productVO.setCategory(productOcrPO.getCategoryPortuguese());
            String description = doSpecialDescription(productOcrPO.getBrandPortuguese(), productOcrPO.getFamilyPortuguese(),
                    productOcrPO.getCategoryPortuguese(), productOcrPO.getDescriptionPortuguese());
            productVO.setDescription(description);
            productVO.setFamily(productOcrPO.getFamilyPortuguese());
        }else if (LanguageEnum.LANGUAGE_RUSSIAN.getKey().equals(language)){
            productVO.setBrand(productOcrPO.getBrandRussian());
            productVO.setCategory(productOcrPO.getCategoryRussian());
            String description = doSpecialDescription(productOcrPO.getBrandRussian(), productOcrPO.getFamilyRussian(),
                    productOcrPO.getCategoryRussian(), productOcrPO.getDescriptionRussian());
            productVO.setDescription(description);
            productVO.setFamily(productOcrPO.getFamilyRussian());
        }
        String url = aliyunOSSClientUtil.getUrlByFileKey(ossClient, productOcrPO.getOssKey());
        productVO.setUrl(url);
    }


    /**
     * 直接返回图搜结果集（5条）
     * @param auctionsList
     * @param productVOS
     * @param limit
     * @param language
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
     * @param language
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
                    if (!StringUtils.isEmpty(productVO1.getKey())){
                        String url = aliyunOSSClientUtil.getUrlByFileKey(ossClient, productVO1.getKey());
                        productVO.setUrl(url);
                    }
                    if (StringUtils.isEmpty(language) || LanguageEnum.LANGUAGE_ENGLISH.getKey().equals(language)){
                        productVO.setBrand(productVO1.getBrand());
                        productVO.setCategory(productVO1.getCategory());
                        productVO.setFamily(productVO1.getFamily());
                        String description = doSpecialDescription(productVO1.getBrand(), productVO1.getFamily(),
                                productVO1.getCategory(), productVO1.getDescription());
                        productVO.setDescription(description);
                    }else if (LanguageEnum.LANGUAGE_CHINESE.getKey().equals(language)){
                        productVO.setBrand(productVO1.getBrandChinese());
                        productVO.setCategory(productVO1.getCategoryChinese());
                        productVO.setFamily(productVO1.getFamilyChinese());
                        String description = doSpecialDescription(productVO1.getBrandChinese(), productVO1.getFamilyChinese(),
                                productVO1.getCategoryChinese(), productVO1.getDescriptionChinese());
                        productVO.setDescription(description);
                    }else if (LanguageEnum.LANGUAGE_PORTUGUESE.getKey().equals(language)){
                        productVO.setBrand(productVO1.getBrandPortuguese());
                        productVO.setCategory(productVO1.getCategoryPortuguese());
                        productVO.setFamily(productVO1.getFamilyPortuguese());
                        String description = doSpecialDescription(productVO1.getBrandPortuguese(), productVO1.getFamilyPortuguese(),
                                productVO1.getCategoryPortuguese(), productVO1.getDescriptionPortuguese());
                        productVO.setDescription(description);
                    }else if (LanguageEnum.LANGUAGE_RUSSIAN.getKey().equals(language)){
                        productVO.setBrand(productVO1.getBrandRussian());
                        productVO.setCategory(productVO1.getCategoryRussian());
                        productVO.setFamily(productVO1.getFamilyRussian());
                        String description = doSpecialDescription(productVO1.getBrandRussian(), productVO1.getFamilyRussian(),
                                productVO1.getCategoryRussian(), productVO1.getDescriptionRussian());
                        productVO.setDescription(description);
                    }
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
     * @throws BizException
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
        request.setNum(50);

        SearchImageResponse response = null;
        try {
            response = client.getAcsResponse(request);
            // todo 分数
//            List<SearchImageResponse.Auction> auctions = response.getAuctions();
//            Iterator<SearchImageResponse.Auction> it = auctions.iterator();
//            while(it.hasNext()){
//                SearchImageResponse.Auction next = it.next();
//                String sortExprValues = next.getSortExprValues();
//                String substring = sortExprValues.substring(0, sortExprValues.indexOf(";"));
//                double l = Double.parseDouble(substring);
//                if (l < 15){
//                    it.remove();
//                }
//            }
        } catch (ClientException e) {
            log.error("阿里云图像搜索接口调用失败,入参SearchImageRequest{}",JSON.toJSONString(request),e);
           throw new BizException(ResultCode.IMAGE_SEARCH_ERROR.getCode(),e.getErrCode());
        }
        return response;
    }

    /**
     * 参数初始化
     * @param multipartFile
     * @return
     * @throws BizException
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

        // 上传图片oss
        Map<String, String> ocr = uploadPicture(multipartFile, "ocr");
        String url = ocr.get("url");

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
     * 上传图片
     * @param multipartFile
     * @param name
     * @return
     * @throws BizException
     */
    private Map<String,String> uploadPicture(MultipartFile multipartFile,String name) throws BizException{
        try {
            Map<String,String> map = new HashMap<>(2);
            OSSClient ossClient = aliyunOSSClientUtil.getOSSClient();
            String dateStr = DateUtils.format(Calendar.getInstance().getTime(),
                    "yyyyMMddHHmmssSSSS");
            String key = MessageFormat.format(name+"/{0}{1}", dateStr + "/", multipartFile.getOriginalFilename());
           String url = aliyunOSSClientUtil.uploadFile(ossClient, multipartFile.getInputStream(), key);
           map.put("key",key);
           map.put("url",url);
           return map;
        } catch (IOException e) {
            log.error("oss上传失败",e);
            throw new  BizException(ResultCode.OSS_UPLOAD_ERROR);
        }
    }


    /**
     * 图片ocr
     * @param multipartFile
     * @throws BizException
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
            if (SUCCESS_CODE == requestCode) {
                for (Object taskResult : taskResults) {
                    //单张图片的处理结果
                    int taskCode = ((JSONObject) taskResult).getIntValue("code");
                    //图片要检测的场景的处理结果, 如果是多个场景，则会有每个场景的结果
                    JSONArray sceneResults = ((JSONObject) taskResult).getJSONArray("results");
                    if (SUCCESS_CODE == taskCode) {
                        for (Object sceneResult : sceneResults) {
                            String scene = ((JSONObject) sceneResult).getString("scene");
                            String suggestion = ((JSONObject) sceneResult).getString("suggestion");
                            if("review" .equals(suggestion) && "ocr".equals(scene)){
                                JSONArray ocrLocations = ((JSONObject) sceneResult).getJSONArray("ocrLocations");
                                if (ocrLocations !=null){
                                    for (Object ocrLocation:ocrLocations ) {
                                        // 识别出的信息
                                        String text = ((JSONObject) ocrLocation).getString("text");
                                        if (!StringUtils.isEmpty(text)){
                                            ocrResults.add(text);
                                        }
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
                //表明请求整体处理失败，原因视具体的情况详细分析
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
     * 新增图搜照片
     * @param productReqData
     * @param multipartFile
     * @return
     * @throws BizException
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

        // 如果多次添加图片具有相同的ProductId + PicName，以最后一次添加为准，前面添加的的图片将被覆盖。
        request.setPicName(multipartFile.getOriginalFilename());
        // 对于通用搜索：不论是否设置类目，系统会将类目设置为88888888。
        request.setCategoryId(88888888);
        // 图片的base64编码
        String encodePicContent = FileUtil.multipartFileToBase64(multipartFile);
        // 必填，图片内容，Base64编码。
        request.setPicContent(encodePicContent);

        // 选填，用户自定义的内容，最多支持 4096个字符。
        Product productSkuPO = new Product();
        productSkuPO.setBrand(productReqData.getBrand());
        productSkuPO.setBrandChinese(productReqData.getBrandChinese());
        productSkuPO.setBrandPortuguese(productReqData.getBrandPortuguese());
        productSkuPO.setBrandRussian(productReqData.getBrandRussian());

        productSkuPO.setDescription(productReqData.getDescription());
        productSkuPO.setDescriptionChinese(productReqData.getDescriptionChinese());
        productSkuPO.setDescriptionPortuguese(productReqData.getDescriptionPortuguese());
        productSkuPO.setDescriptionRussian(productReqData.getDescriptionRussian());

        productSkuPO.setCategory(productReqData.getCategory());
        productSkuPO.setCategoryChinese(productReqData.getCategoryChinese());
        productSkuPO.setCategoryPortuguese(productReqData.getCategoryPortuguese());
        productSkuPO.setCategoryRussian(productReqData.getCategoryRussian());

        productSkuPO.setFamily(productReqData.getFamily());
        productSkuPO.setFamilyChinese(productReqData.getFamilyChinese());
        productSkuPO.setFamilyPortuguese(productReqData.getFamilyPortuguese());
        productSkuPO.setFamilyRussian(productReqData.getFamilyRussian());

        Map<String, String> newPicture = uploadPicture(multipartFile, "newPicture");
        productSkuPO.setKey(newPicture.get("key"));

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

    /**
     * 查询产品
     * @param productReqData
     * @return
     */
    public List<ProductVO> searchProductBySkuOrCategory(ProductReqData productReqData){
        List<ProductSkuPO> productSkuPOS = new ArrayList<>();
        productReqData.setSearchCriteria(StringOperationUtil.sqlReplace(productReqData.getSearchCriteria()));
        List<ProductVO> productVOS = new ArrayList<>();
        ProductSkuPO skuPO = productSkuMapper.selectProductByReference(productReqData.getSearchCriteria());
        // 通过sku查询产品
        if (skuPO != null){
            productSkuPOS.add(skuPO);
            // 非施耐德产品
            if (StringUtils.isEmpty(skuPO.getOssKey())){
                // 通过非施耐德sku查询施耐德产品列表
                productSkuPOS = listsNonSeProduct(productReqData.getSearchCriteria(),productSkuPOS);
            }
            OSSClient ossClient = aliyunOSSClientUtil.getOSSClient();
            for (ProductSkuPO productSkuPO : productSkuPOS) {
                ProductVO productVO = new ProductVO();
                init(productVO,productSkuPO,productReqData.getLanguage(),ossClient);
                productVOS.add(productVO);
            }
        }
        return productVOS;
    }

    /**
     * 通过非施耐德sku查询施耐德产品列表
     * @param sku
     * @param productSkuPOS
     * @return
     */
    private List<ProductSkuPO> listsNonSeProduct(String sku,List<ProductSkuPO> productSkuPOS){
        List<SkuMatchingPO> skuMatchingPOS = skuMatchingMapper.selectMatchByCompetitorSku(sku);
        if (!CollectionUtils.isEmpty(skuMatchingPOS)){
            List<String> skuList = skuMatchingPOS.stream().map(SkuMatchingPO::getSchneiderElectricSku).collect(toList());
            List<ProductSkuPO> skuPOS = productSkuMapper.listProductsBySku(skuList);
            productSkuPOS.addAll(skuPOS);
        }
        return productSkuPOS;
    }
}
