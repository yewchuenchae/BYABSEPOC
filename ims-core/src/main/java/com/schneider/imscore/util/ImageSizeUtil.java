package com.schneider.imscore.util;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author liyuan
 * @createDate 2019/08/28 16:50
 * @Description
 */
@Slf4j
public class ImageSizeUtil {

    /**
     * 支持上传的图片后缀
     */
    private static final String[] SUFFIXS   = {"png", "jpg","jpeg"};


    /**
     * 获取图片最长边长度
     * @param params
     * @return
     */
    public static int getImageLengthOfSide(MultipartFile params){
        int lengthSize = 3000;
        Map<String, Integer> result = new HashMap<>();
        // 获取图片格式
        String suffixName = getSuffixNameInfo(params);
        try {
            Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName(suffixName);
            ImageReader reader = (ImageReader) readers.next();
            ImageInputStream iis = ImageIO.createImageInputStream(params.getInputStream());
            reader.setInput(iis, true);
            result.put("width",reader.getWidth(0));
            result.put("height",reader.getHeight(0));
            if(reader.getWidth(0) > reader.getHeight(0)){
                lengthSize = reader.getWidth(0);
            }else{
                lengthSize = reader.getHeight(0);
            }
        } catch (IOException e) {
            log.error("图片压缩失败，suffixName :{}",suffixName,e);
        }

        return lengthSize;
    }

    /**
     * 获取图片格式
     * @param params
     * @return
     */
    public static String getSuffixNameInfo(MultipartFile params){
        String result = "";
        String originalFilename = params.getOriginalFilename();

        // 图片后缀
        String suffixName = originalFilename.substring(
                originalFilename.lastIndexOf(".")).toLowerCase();
        log.info("suffixName后缀"+suffixName);
        if(suffixName.indexOf("png")>0){
            result = "png";
        }else if(suffixName.indexOf("jpg")>0){
            result = "jpg";
        }else if (suffixName.indexOf("jpeg")>0){
            result = "jpg";
        }

        return result;
    }



    /**
     * 根据指定大小压缩图片
     *
     * @param imageBytes  源图片字节数组
     * @param desFileSize 指定图片大小，单位kb
     * @return 压缩质量后的图片字节数组
     */
    public static byte[] compressPicForScale(byte[] imageBytes,int imageLengthSize) {
        if (imageBytes == null || imageBytes.length <= 0) {
            return imageBytes;
        }
//        long srcSize = imageBytes.length;
        double accuracy = 0.5;
        double v = imageLengthSize;
        try {

            do {
                ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream(imageBytes.length);
                Thumbnails.of(inputStream)
                        .scale(accuracy)
                        .outputQuality(accuracy)
                        .toOutputStream(outputStream);
                imageBytes = outputStream.toByteArray();
                v = v * accuracy;
            }while (v>1024);

        } catch (Exception e) {
            log.error("【图片压缩】msg=图片压缩失败!", e);
        }
        return imageBytes;
    }



    /**
     * base64 转MultipartFile
     * @param base64
     * @return
     */
    public static MultipartFile base64ToMultipart(String base64) {
        try {
            // 注意base64是否有头信息，如：data:image/jpeg;base64,。。。
            String[] baseStrs = base64.split(",");

            BASE64Decoder decoder = new BASE64Decoder();
            byte[] b = new byte[0];
            b = decoder.decodeBuffer(baseStrs[1]);

            for(int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }

            return new BASE64DecodedMultipartFile(b, baseStrs[0]);
        } catch (IOException e) {
            log.error("图片压缩失败",e);
            return null;
        }
    }


    /**
     * 压缩图片
     * @return
     */
    public static MultipartFile[] byte2Base64StringFun(MultipartFile[] fileImg){
        MultipartFile[] result = fileImg;
        // 获取图片最长边
        int imageLengthSize = ImageSizeUtil.getImageLengthOfSide(fileImg[0]);
        Long swd = fileImg[0].getSize();
        // 像素大于1024 或 大于2M
        if(imageLengthSize > 1024 || swd > 2097152){
            BASE64Encoder encoder = new BASE64Encoder();
            String imgData1 = null;
            try {
                InputStream inputStream = fileImg[0].getInputStream();
                byte[] imgData = ImageSizeUtil.compressPicForScale(ToolsUtil.getByteArray(inputStream),imageLengthSize);
                imgData1 = "data:"+fileImg[0].getContentType()+";base64,"+encoder.encode(imgData);
                MultipartFile def = ImageSizeUtil.base64ToMultipart(imgData1);
                result[0] = def;
            } catch (IOException e) {
                log.error("压缩图片出错",e);
            }
        }

        return result;
    }

    /**
     * 校验支持的文件格式
     * @param fileName
     * @return 支持返回true
     */
    public static Boolean checkSuffix(String fileName) {
        if (StringUtils.isBlank(fileName)) {
            return Boolean.FALSE;
        }
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        for (String string : SUFFIXS) {
            if (StringUtils.equalsIgnoreCase(string, suffix)) {
                return true;
            }
        }
        return false;
    }


}
