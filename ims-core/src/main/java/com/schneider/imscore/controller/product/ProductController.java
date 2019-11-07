package com.schneider.imscore.controller.product;

import com.schneider.imscore.biz.service.product.ProductService;
import com.schneider.imscore.resp.Result;
import com.schneider.imscore.vo.product.req.ProductReqData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * @author liyuan
 * @createDate 2019/08/22 11:08
 * @Description 产品搜索Controller
 */
@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * 图搜
     * @param request
     * @param language
     * @return
     */
    @PostMapping("/product/search")
    public Result listProductsBySearch(HttpServletRequest request,String language,String schneider){
        long currentTimeMillis = System.currentTimeMillis();
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFile("file");
        return productService.listProductsBySearch(file,language,request,currentTimeMillis,schneider);
    }

    /**
     * 新增图搜图片
     * @param request
     * @param productReqData
     * @return
     */
    @PostMapping("/product/search/add")
    public Result addImageSearch(HttpServletRequest request, ProductReqData productReqData){
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFile("file");
        return productService.saveImageSearch(productReqData,file);
    }

    /**
     * 日志展示
     * @return
     */
    @GetMapping("/product/search/log")
    public Result getLog(){
        return productService.getLog();
    }

    /**
     * 通过sku查询产品
     * @param productReqData
     * @return
     */
    @GetMapping("/product/search/fuzzy")
    public Result listProductsFuzzySearch(ProductReqData productReqData){
        return productService.listProductsFuzzySearch(productReqData);
    }
}
