/**
 * BEYONDSOFT.COM INC
 */
package com.schneider.imscore.util;

import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.schneider.imscore.constant.Constant;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;

/**
 *
 * @Description OSS存储客户端
 * @author yulijun
 * @version $Id: AliyunOSSClientUtil.java, v 0.1 2018/6/7 13:54 yulijun Exp $$
 */
@Component
public class AliyunOSSClientUtil {
    //阿里云API的外网域名
    @Value("${aliyun.oss.endpoint}")
    private  String endPointOut;
    //阿里云API的密钥Access Key ID
    @Value("${aliyun.oss.accessKeyId}")
    private  String accessKeyId;
    //阿里云API的密钥Access Key Secret
    @Value("${aliyun.oss.accessKeySecret}")
    private  String  accessKeySecret;
    //阿里云API的bucket名称
    @Value("${aliyun.oss.bucketName}")
    private  String bucketName;



    /**
     * 连接客户端
     * @return
     */
    public  OSSClient getOSSClient() {
        return new OSSClient(endPointOut, AESUtil.decrypt(accessKeyId, Constant.KEY), AESUtil.decrypt(accessKeySecret,Constant.KEY));
    }

    /**
     * 关闭客户端连接
     */
    public static void closeConnect(OSSClient client) {
        if (client != null) {
            client.shutdown();
        }
    }



    /**
     * 上传文件并且返回访问路径
     * @param ossClient 云连接
     * @param is        文件流
     * @param fileKey   图片唯一key  带上后缀 .jpg .png .gif等
     * @return
     */
    public  String uploadFile(OSSClient ossClient, InputStream is, String fileKey) {
        ossClient.putObject(bucketName, fileKey, is);
        return getUrlByFileKey(ossClient, fileKey);
    }

    /**
     * 根据文件key获取访问url  有时间限制
     * @param ossClient     云连接
     * @param key
     * @return
     */
    public  String getUrlByFileKey(OSSClient ossClient, String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        try {
            GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, key,
                HttpMethod.GET);
            //附件地址有效期
            request.setExpiration(new Date(System.currentTimeMillis() + 90 * 60 * 1000));
            URL url = ossClient.generatePresignedUrl(request);
            if (url == null) {
                return "获取文件访问url出错";
            }
            return getHttpsUrl(url.toURI().toString());
        } catch (URISyntaxException e) {
            return "获取文件访问url出错";
        }
    }



    /**
     * http转换成https
     * @param url
     * @return
     */
    private static String getHttpsUrl(String url) {
        return url.substring(0, url.indexOf(":")) + "s" + url.substring(url.indexOf(":"));
    }


}
