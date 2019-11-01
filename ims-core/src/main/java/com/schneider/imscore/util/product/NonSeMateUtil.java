package com.schneider.imscore.util.product;

import com.alibaba.fastjson.JSON;
import com.schneider.imscore.mapper.product.ProductSkuMapper;
import com.schneider.imscore.po.product.ProductSkuPO;
import com.schneider.imscore.resp.Result;
import com.schneider.imscore.util.excel.ExcelProduct;
import com.schneider.imscore.util.excel.ExcelSkuMatching;
import com.schneider.imscore.util.excel.ExcelUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author liyuan
 * @createDate 2019/10/15 17:14
 * @Description
 */
@RestController
public class NonSeMateUtil {

    @Autowired
    private ProductSkuMapper productSkuMapper;


    /**
     * 非施耐德产品生产mate文件
     * @param request
     */
    @PostMapping("/util/mate")
    public Result saveMatches(HttpServletRequest request){
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFile("file");
        InputStream is = null;
        String fileName = file.getOriginalFilename();
        try {
            is = file.getInputStream();
            ExcelUtil<ExcelSkuMatching> excelUtil1 = new ExcelUtil<>(ExcelSkuMatching.class);
            List<ExcelSkuMatching> excelSkuMatchings = excelUtil1.importExcel(fileName, is, 1);
            File file1 = new File("C:\\Users\\liyuan11\\Desktop\\Siemens_POC");
            String mateFile = mateFile(file1,excelSkuMatchings);
            System.out.println(mateFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.buildSuccess();
    }

    /**
     * 非施耐德产品  mate文件
     * @param file
     * @param excelSkuMatchings
     * @return
     */
    private static String mateFile(File file,List<ExcelSkuMatching> excelSkuMatchings){
        String mateFile = "";

        Meta meta = new Meta();
        if (file != null){
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                File file1 = files[i];
                String file1Name = file1.getName();
                meta.setItem_id(file1Name);
                meta.setCat_id("88888888");
                meta.setOperator("ADD");
                File[] files2 = file1.listFiles();
                for (File file2: files2) {
                    String path = file2.getAbsolutePath();
                    String substring = path.substring(38, path.length()).replace("\\","/");
                    List<String> pic_list = new ArrayList<>();
                    pic_list.add(substring);
                    Product product = new Product();
                    product.setBrand("Siemens");
                    product.setCategory("Variable Speed Drives");
                    product.setFamily("SINAMICS V20");

                    for (int j = 0; j < excelSkuMatchings.size(); j++) {
                        String schneiderElectricSKU = excelSkuMatchings.get(i).getSchneiderElectricSKU();
                        product.setDescription(schneiderElectricSKU.substring(0,schneiderElectricSKU.indexOf(",")));
                    }
                    meta.setCust_content(JSON.toJSONString(product));
                    meta.setPic_list(pic_list);
                    mateFile = mateFile.concat(JSON.toJSONString(meta)+"\n");
                }
            }
        }
        return mateFile;
    }

    /**
     * 非施耐德产品初始化数据库
     * @param request
     */
    @PostMapping("/util/saveImageSearchProduct")
    public Result saveImageSearchProduct(HttpServletRequest request){
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFile("file");
        InputStream is = null;
        String fileName = file.getOriginalFilename();
        try {
            is = file.getInputStream();
            ExcelUtil<ExcelProduct> excelUtil1 = new ExcelUtil<>(ExcelProduct.class);
            List<ExcelProduct> excelSkuMatchings = excelUtil1.importExcel(fileName, is, 1);
            for (int i = 0; i < excelSkuMatchings.size(); i++) {
                ExcelProduct excelSkuMatching = excelSkuMatchings.get(i);

                ProductSkuPO productSkuPO = new ProductSkuPO();
                productSkuPO.setReference(excelSkuMatching.getReference());
                String description = excelSkuMatching.getDescription();

                productSkuPO.setDescription(description);
                String replaceAll = description.replaceAll(" ", "");
                productSkuPO.setDescriptionOcr(replaceAll);

                productSkuPO.setBrand(excelSkuMatching.getBrand());
                productSkuPO.setFamily(excelSkuMatching.getFamily());
                productSkuPO.setCategory(excelSkuMatching.getCategory());
                productSkuPO.setCreated(new Date());
                productSkuPO.setModified(new Date());

                productSkuMapper.insertSelective(productSkuPO);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.buildSuccess();
    }

    /**
     * 非施耐德产品mate
     * @param request
     */
    @PostMapping("/util/non/mate")
    public Result nonSeMate(HttpServletRequest request){
        String filePath = "C:\\Users\\liyuan11\\Desktop\\ATV12 edited";
        String mateFile = "";
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFile("file");
        try {
            String fileName = file.getOriginalFilename();
            InputStream is1 = file.getInputStream();
            ExcelUtil<ExcelProduct> excelUtil1 = new ExcelUtil<>(ExcelProduct.class);
            List<ExcelProduct> excelProducts = excelUtil1.importExcel(fileName, is1, 1);
            for (ExcelProduct excelProduct: excelProducts) {
                if (StringUtils.isNotBlank(excelProduct.getReference())){
                    Meta meta = new Meta();
                    meta.setItem_id(excelProduct.getReference());
                    meta.setCat_id("88888888");
                    meta.setOperator("ADD");

                    Product product1 = new Product();
                    product1.setBrand(excelProduct.getBrand());
                    product1.setCategory(excelProduct.getCategory());
                    product1.setFamily(excelProduct.getFamily());
                    product1.setDescription(excelProduct.getDescription());
                    String toJSONString = JSON.toJSONString(product1);
                    meta.setCust_content(toJSONString);

                    String filePathExcel = excelProduct.getFilePath();
                    List<String> readfile = readfile(filePath+ filePathExcel);
                    for (int i = 0; i < readfile.size(); i++) {
                        List<String> names = new ArrayList<>();
                        names.add((filePathExcel.substring(1)+"\\"+readfile.get(i)).replace("\\","/"));
                        if (names.size() == 0){
                            meta.setPic_list(null);
                        }else {
                            meta.setPic_list(names);
                            meta.setCat_id("88888888");
                            mateFile = mateFile.concat(JSON.toJSONString(meta)+"\n");
                        }
                    }
                }
            }


            System.out.println(mateFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.buildSuccess();
    }


    /**
     * 读取某个文件夹下的所有文件
     */
    public static List<String> readfile(String filepath) throws FileNotFoundException, IOException {
        List<String> list = new ArrayList<>();
        try {
            File file = new File(filepath);
            if (!file.isDirectory()) {

            } else if (file.isDirectory()) {
                String[] filelist = file.list();
                for (int i = 0; i < filelist.length; i++) {
                    File readfile = new File(filepath + "\\" + filelist[i]);
                    if (!readfile.isDirectory()) {
                        list.add(readfile.getName());

                    } else if (readfile.isDirectory()) {
                        readfile(filepath + "\\" + filelist[i]);
                    }
                }

            }
        } catch (FileNotFoundException e) {
            System.out.println("readfile()   Exception:" + e.getMessage());
        }
        return list;
    }


}
