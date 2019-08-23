package com.schneider.imscore.biz.service.product;

import com.schneider.imscore.biz.manager.product.ProductManager;
import com.schneider.imscore.resp.Result;
import com.schneider.imscore.resp.ResultCode;
import com.schneider.imscore.resp.exception.BizException;
import com.schneider.imscore.vo.product.ProductVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author liyuan
 * @createDate 2019/08/22 11:06
 * @Description
 */
@Service
@Slf4j
public class ProductService {
    @Autowired
    private ProductManager productManager;

    /**
     * 产品搜索
     * @param multipartFile
     * @return
     */
    public Result listProductsBySearch(MultipartFile multipartFile){
        List<ProductVO> productVOS = null;
        try {
            if (multipartFile == null){
                return new Result(ResultCode.ILLEGAL_PARAM.getCode(),ResultCode.ILLEGAL_PARAM.getDesc());
            }
            productVOS = productManager.listProductsBySearch(multipartFile);
        } catch (BizException e){
            log.error("产品列表查询失败",e);
            return new Result(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("产品列表查询失败",e);
            return new Result<>(ResultCode.FAILED.getCode(),ResultCode.FAILED.getDesc());
        }
        return Result.buildSuccess(productVOS);
    }
}
