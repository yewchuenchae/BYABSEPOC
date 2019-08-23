package com.schneider.imscore.biz.manager.product;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.green.model.v20180509.ImageSyncScanRequest;
import com.aliyuncs.http.FormatType;
import com.aliyuncs.http.HttpResponse;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.imagesearch.model.v20190325.SearchImageRequest;
import com.aliyuncs.imagesearch.model.v20190325.SearchImageResponse;
import com.aliyuncs.green.extension.uploader.ClientUploader;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.schneider.imscore.resp.ResultCode;
import com.schneider.imscore.resp.exception.BizException;
import com.schneider.imscore.util.FileUtil;
import com.schneider.imscore.vo.product.ProductVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import java.util.*;


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


    /**
     * 图片搜索
     * @param multipartFile
     * @return
     * @throws BizException
     * @throws ClientException
     */
    public List<ProductVO> listProductsBySearch(MultipartFile multipartFile) throws BizException,ClientException {
        List<ProductVO> productVOS = new ArrayList<>();
       // 1.图片审核
//        checkImage(multipartFile);

        // 2.图片搜索
        // 接口开始时间
        long start = System.currentTimeMillis();
        SearchImageResponse response = imageSearch(multipartFile);
        long end = System.currentTimeMillis();
        log.info("image search sdk执行时间：{}ms",end-start);

        if (response != null){
            List<SearchImageResponse.Auction> auctions = response.getAuctions();
            if (!CollectionUtils.isEmpty(auctions)){
                for (SearchImageResponse.Auction auction:auctions) {
                    ProductVO productVO = new ProductVO();
                    productVO.setProductId(auction.getProductId());
//                    String customContent = auction.getCustomContent();
                    // todo mock 数据
                    String customContent = "{\"brand\":\"Schneider Electric\",\"category\":\"Variable Speed Drive\",\"description\":\"Schneider Electric Altivar 320 Variable Speed Drive 1-Phase, 200-240 V AC, 0.18 kW, 0.25 hp, 1.5 A, Compact\",\"family\":\"Altivar 320\"}";
                   // customContent 为json字符串
                    ProductVO productVO1 = JSON.parseObject(customContent, ProductVO.class);
                    if (productVO1 != null){
                        productVO.setBrand(productVO1.getBrand());
                        productVO.setCategory(productVO1.getCategory());
                        productVO.setDescription(productVO1.getDescription());
                        productVO.setFamily(productVO1.getFamily());
                    }

                    productVOS.add(productVO);
                }
            }
        }
        // 3.获取产品列表
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
        // 1. 对于商品搜索：若设置类目，则以设置的为准；若不设置类目，将由系统进行类目预测，预测的类目结果可在Response中获取 。
        // 2. 对于通用搜索：不论是否设置类目，系统会将类目设置为88888888。
        request.setCategoryId(88888888);
        // 主体识别
        request.setCrop(true);

        // 选填，返回结果的数目。取值范围：1-100。默认值：10。
        request.setNum(10);
        // 选填，返回结果的起始位置。取值范围：0-499。默认值：0。
        request.setStart(0);

        SearchImageResponse response = null;
        response = client.getAcsResponse(request);
        return response;
    }

    /**
     * 图片审核ocr
     * @param multipartFile
     * @throws Exception
     */
    private void checkImage(MultipartFile multipartFile) throws BizException,ClientException{
        IClientProfile profiles = DefaultProfile.getProfile(region, accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint(region, "Green", endpoint);
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
         * 设置要检测的场景, 计费是按照该处传递的场景进行
         * 一次请求中可以同时检测多张图片，每张图片可以同时检测多个风险场景，计费按照场景计算
         * 例如：检测2张图片，场景传递porn,terrorism，计费会按照2张图片鉴黄，2张图片暴恐检测计算
         * porn: porn表示色情场景检测
         */
        httpBody.put("scenes", Arrays.asList("porn","terrorism"));

        /**
         * 如果您要检测的文件存于本地服务器上，可以通过下述代码片生成url，
         * 再将返回的url作为图片地址传递到服务端进行检测
         */
        ClientUploader uploader = ClientUploader.getImageClientUploader(client);
        byte[] imageBytes = null;
        String url = null;
        try{
            //这里读取本地文件作为二进制数据，当做输入做为示例, 实际使用中请直接替换成您的图片二进制数据
            imageBytes = FileUtils.readFileToByteArray(FileUtil.multipartFileToFile(multipartFile));
            //上传到服务端
            url = uploader.uploadBytes(imageBytes);
        }catch (Exception e){
            e.printStackTrace();
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

        imageSyncScanRequest.setHttpContent(org.apache.commons.codec.binary.StringUtils.getBytesUtf8(httpBody.toJSONString()),
                "UTF-8", FormatType.JSON);

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
            System.out.println(JSON.toJSONString(scrResponse, true));
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
                            if (StringUtils.isNotBlank(suggestion)){
                                if ("block".equals(suggestion)){
                                    throw new BizException(ResultCode.IMAGE_BLOCK);
                                }
                            }
                            //根据scene和suggetion做相关处理
                            //do something
                            System.out.println("scene = [" + scene + "]");
                            System.out.println("suggestion = [" + suggestion + "]");
                        }
                    } else {
                        //单张图片处理失败, 原因是具体的情况详细分析
                        throw new BizException(ResultCode.SINGLE_IMAGE_CHECK_ERROR);
                        log.info("检测图片单张图片处理失败 task response:{}",JSON.toJSONString(taskResult));
                    }
                }
            } else {
                /**
                 * 表明请求整体处理失败，原因视具体的情况详细分析
                 */
                throw new BizException(ResultCode.WHOLE_IMAGE_CHECK_ERROR);
                log.info("the whole image scan request failed. response:{}",JSON.toJSONString(scrResponse));
            }
        }
    }

}
