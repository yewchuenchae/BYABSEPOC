package com.schneider.imscore.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author liyuan
 * @createDate 2019/08/22 11:34
 * @Description 文件工具类
 */
@Slf4j
public class FileUtil {


    /**
     * file转Base64
     * @param file
     * @return
     */
    public static String multipartFileToBase64(MultipartFile file) {
        String base64EncoderImg = null;
        try {
            BASE64Encoder base64Encoder =new BASE64Encoder();
            base64EncoderImg = base64Encoder.encode(file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return base64EncoderImg.replaceAll("[\\s*\t\n\r]", "");
    }

}
