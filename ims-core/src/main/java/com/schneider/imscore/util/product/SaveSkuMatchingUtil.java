package com.schneider.imscore.util.product;


import com.schneider.imscore.mapper.product.SkuMatchingMapper;
import com.schneider.imscore.po.product.SkuMatchingPO;
import com.schneider.imscore.util.excel.ExcelSkuMatching;
import com.schneider.imscore.util.excel.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.List;

/**
 * @author liyuan
 * @createDate 2019/10/15 16:00
 * @Description
 */
@RestController
public class SaveSkuMatchingUtil {

    @Autowired
    private SkuMatchingMapper skuMatchingMapper;


    @PostMapping("/util/saveMatches")
    public void saveMatches(HttpServletRequest request){
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFile("file");
        InputStream is = null;
        String fileName = file.getOriginalFilename();
        try {
            is = file.getInputStream();
            ExcelUtil<ExcelSkuMatching> excelUtil1 = new ExcelUtil<>(ExcelSkuMatching.class);
            List<ExcelSkuMatching> excelSkuMatchings = excelUtil1.importExcel(fileName, is, 1);
            for (ExcelSkuMatching excelSkuMatching: excelSkuMatchings) {
                SkuMatchingPO skuMatchingPO = new SkuMatchingPO();
                skuMatchingPO.setCompetitorSku(excelSkuMatching.getCompetitorSKU());
                skuMatchingPO.setSchneiderElectricSku(excelSkuMatching.getSchneiderElectricSKU());
                skuMatchingMapper.saveSkuMatching(skuMatchingPO);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
