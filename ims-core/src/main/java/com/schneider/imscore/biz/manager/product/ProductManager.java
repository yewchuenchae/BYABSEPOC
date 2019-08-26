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
import com.schneider.imscore.resp.ResultCode;
import com.schneider.imscore.resp.exception.BizException;
import com.schneider.imscore.util.AliyunOSSClientUtil;
import com.schneider.imscore.util.BeanConvertUtils;
import com.schneider.imscore.util.DateUtils;
import com.schneider.imscore.util.FileUtil;
import com.schneider.imscore.vo.product.ProductVO;
import com.schneider.imscore.vo.product.req.ProductReqData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;

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


    /**
     * 图片搜索
     * @param multipartFile
     * @return
     * @throws BizException
     * @throws ClientException
     */
    public List<ProductVO> listProductsBySearch(MultipartFile multipartFile) throws BizException,ClientException {
        List<ProductVO> productVOS = new ArrayList<>();
       // 1.图片ocr
        List<String> text = imageOcr(multipartFile);

//        // 2.图片搜索
        SearchImageResponse response = imageSearch(multipartFile);

        if (response != null){
            // 直接返回结果集
            if (CollectionUtils.isEmpty(text)){
                productVOS = productVOList(response.getAuctions(), productVOS);
            }else {
                    List<SearchImageResponse.Auction> auctionsList = response.getAuctions();
                    if (!CollectionUtils.isEmpty(auctionsList)){
                        // ocr结果过滤
                        List<SearchImageResponse.Auction> filter = new ArrayList<>();
                        List<SearchImageResponse.Auction> filterResult = filterAction(filter, text);
                        // ocr结果查到的过滤后没有结果
                        if (filterResult.size() == 0 ){
                            // 1.text作为sku查询数据库 有记录唯一范围

                            // 2 数据库查不到则返回结果集
                            productVOS = productVOList(response.getAuctions(), productVOS);

                            // 超过5条
                        } else if (filterResult.size() > IMAGE_SEARCH_RESULT_LIMIT){
                             productVOS = productVOList(filterResult, productVOS);
                        }else {
                            productVOS = BeanConvertUtils.convertList(auctionsList, ProductVO.class);
                        }
                    }
            }
        }
        // 3.返回搜索结果列表
        return productVOS;
    }

    /**
     * 返回ocr过滤后的结果
     * @param auctionsList
     * @param text
     * @return
     */
    private List<SearchImageResponse.Auction> filterAction(List<SearchImageResponse.Auction> auctionsList,List<String> text){
        for (SearchImageResponse.Auction auction : auctionsList) {
            for (String str: text) {
                if (auction.getCustomContent().contains(str)){
                    auctionsList.add(auction);
                    break;
                }
            }
        }
        return auctionsList;
    }



    /**
     * 直接返回图搜结果集（5条）
     * @param auctionsList
     * @param productVOS
     * @return
     */
    private List<ProductVO> productVOList(List<SearchImageResponse.Auction> auctionsList,List<ProductVO> productVOS){
        if (!CollectionUtils.isEmpty(auctionsList)){
            List<SearchImageResponse.Auction> auctions = new ArrayList<>();
            if (auctionsList.size() > IMAGE_SEARCH_RESULT_LIMIT){
                auctions = auctionsList.subList(0, IMAGE_SEARCH_RESULT_LIMIT);
                productVOS = BeanConvertUtils.convertList(auctions, ProductVO.class);
            }else {
                productVOS = BeanConvertUtils.convertList(auctionsList, ProductVO.class);
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
    private SearchImageResponse imageSearch(MultipartFile multipartFile) throws ClientException{
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
        // 2. 对于通用搜索：不论是否设置类目，系统会将类目设置为88888888。
        request.setCategoryId(88888888);
        // 主体识别
        request.setCrop(true);

        // 选填，返回结果的数目。取值范围：1-100。默认值：10。
        request.setNum(20);

        SearchImageResponse response = null;
        response = client.getAcsResponse(request);
        return response;
    }

    /**
     * 图片ocr
     * @param multipartFile
     * @throws Exception
     */
    private List<String> imageOcr(MultipartFile multipartFile) throws BizException,ClientException{
        List<String> ocrResults = new ArrayList<>();
        IClientProfile profiles = DefaultProfile.getProfile(region, accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint(region, "Green", greenEndpoint);
        IAcsClient client = new DefaultAcsClient(profiles);

        ImageSyncScanRequest imageSyncScanRequest = new ImageSyncScanRequest();
        // 指定api返回格式
        imageSyncScanRequest.setAcceptFormat(FormatType.JSON);
        // 指定请求方法
        imageSyncScanRequest.setMethod(MethodType.POST);
        imageSyncScanRequest.setEncoding("utf-8");
        //支持http和https
        imageSyncScanRequest.setProtocol(ProtocolType.HTTP);


        JSONObject httpBody = new JSONObject();
        /**
         * 设置待检测图片， 一张图片一个task，最多支持100张图片同时检测，即需要构建100个task
         * 多张图片同时检测时，处理的时间由最后一个处理完的图片决定。因此通常情况下批量检测的平均rt比单张检测的要长, 一次批量提交的图片数越多，rt被拉长的概率越高
         * 这里以单张图片检测作为示例, 如果是批量图片检测，请自行构建多个task
         * 本地图片相对于互联网图片链接来说，多了一个上传步骤，上传后取返回的链接进行检测
         */
        httpBody.put("scenes", Arrays.asList("ocr"));

        /**
         * 如果您要检测的文件存于本地服务器上，可以通过下述代码片生成url，
         * 再将返回的url作为图片地址传递到服务端进行检测
         */
        String url = null;
        try {
            OSSClient ossClient = aliyunOSSClientUtil.getOSSClient();
            String dateStr = DateUtils.format(Calendar.getInstance().getTime(),
                    "yyyyMMddHHmmssSSSS");
            String key = MessageFormat.format("ocr/{0}{1}", dateStr + "/", multipartFile.getOriginalFilename());
            url = aliyunOSSClientUtil.uploadFile(ossClient, multipartFile.getInputStream(), key);
        } catch (IOException e) {

        }


        /**
         * 设置待检测图片， 一张图片一个task，
         * 多张图片同时检测时，处理的时间由最后一个处理完的图片决定。
         * 通常情况下批量检测的平均rt比单张检测的要长, 一次批量提交的图片数越多，rt被拉长的概率越高
         * 这里以单张图片检测作为示例, 如果是批量图片检测，请自行构建多个task
         */
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
        HttpResponse httpResponse = null;

        httpResponse = client.doAction(imageSyncScanRequest);

        //服务端接收到请求，并完成处理返回的结果
        if (httpResponse != null && httpResponse.isSuccess()) {
            JSONObject scrResponse = JSON.parseObject(org.apache.commons.codec.binary.StringUtils.newStringUtf8(httpResponse.getHttpContent()));
            System.out.println(JSON.toJSONString(scrResponse,true));
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
                        log.info("检测图片单张图片处理失败 task response:{}",JSON.toJSONString(taskResult));
                        throw new BizException(ResultCode.SINGLE_IMAGE_CHECK_ERROR);
                    }
                }
            } else {
                /**
                 * 表明请求整体处理失败，原因视具体的情况详细分析
                 */
                log.error("the whole image scan request failed. response:{}",JSON.toJSONString(scrResponse));
                throw new BizException(ResultCode.WHOLE_IMAGE_CHECK_ERROR);
            }
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
    public AddImageResponse saveImageSearch(ProductReqData productReqData,MultipartFile multipartFile) throws ClientException{
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
        // 选填，图片类目。
        // 2. 对于通用搜索：不论是否设置类目，系统会将类目设置为88888888。
        request.setCategoryId(88888888);
        // 图片的base64编码
        String encodePicContent = FileUtil.multipartFileToBase64(multipartFile);
        request.setPicContent(encodePicContent);
        // 必填，图片内容，Base64编码。
        // 最多支持 2MB大小图片以及5s的传输等待时间。当前仅支持jpg和png格式图片；图片
        // 长和宽的像素必须都大于等于200，并且小于等于1024；图像中不能带有旋转信息。
        request.setPicContent(encodePicContent);

        // 选填，用户自定义的内容，最多支持 4096个字符。
        // 查询时会返回该字段。例如可添加图片的描述等文本。
        request.setCustomContent(productReqData.getCustomContent());
        return  client.getAcsResponse(request);
    }

}
