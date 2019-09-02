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
     * MultipartFile转File
     * @param multipartFile
     * @return
     * @throws Exception
     */
    public static File multipartFileToFile(MultipartFile multipartFile)  {
        File f = null;
        try {
            f=File.createTempFile("tmp", null);
            multipartFile.transferTo(f);
        } catch (IOException e) {
            log.error("MultipartFile转File失败",e);
        }
        if (f != null){
            //使用完成删除文件
            f.deleteOnExit();
        }
        return f;
    }


    /**
     * MultipartFile转Base64
     * @param multipartFile
     * @return
     * @throws Exception
     */
    public static String multipartFileToBase64(MultipartFile multipartFile) {
        File f = multipartFileToFile(multipartFile);
        String base64= null;
        byte[] buffer = new byte[0];
        try {
            FileInputStream inputFile = new FileInputStream(f);
            buffer = new byte[(int) f.length()];
            inputFile.read(buffer);
            inputFile.close();
        } catch (IOException e) {
            log.error("MultipartFile转Base64失败",e);
        }
        base64=new BASE64Encoder().encode(buffer);
        return  base64.replaceAll("[\\s*\t\n\r]", "");
    }
}
