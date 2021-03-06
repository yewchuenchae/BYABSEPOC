package com.schneider.imscore.biz.service.product;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.imagesearch.model.v20190325.AddImageResponse;
import com.schneider.imscore.biz.manager.log.ImageSearchLogManager;
import com.schneider.imscore.biz.manager.product.ProductManager;
import com.schneider.imscore.resp.Result;
import com.schneider.imscore.resp.ResultCode;
import com.schneider.imscore.resp.exception.BizException;
import com.schneider.imscore.util.ImageSizeUtil;
import com.schneider.imscore.vo.log.ImageSearchLogVO;
import com.schneider.imscore.vo.product.ProductVO;
import com.schneider.imscore.vo.product.req.ProductReqData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
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

    @Autowired
    private ImageSearchLogManager imageSearchLogManager;

    /**
     * 产品搜索
     * @param multipartFile
     * @return
     */
    public Result listProductsBySearch(MultipartFile multipartFile, String language, HttpServletRequest httpServletRequest,Long currentTimeMillis,String schneider){

        List<ProductVO> productVOS = null;
        try {
            if (multipartFile == null){
                return new Result(ResultCode.ILLEGAL_PARAM.getCode(),ResultCode.ILLEGAL_PARAM.getDesc());
            }

            if (!ImageSizeUtil.checkSuffix( multipartFile.getOriginalFilename())){
                return new Result(ResultCode.ILLEGAL_PARAM.getCode(),ResultCode.ILLEGAL_PARAM.getDesc());
            }
            productVOS = productManager.listProductsBySearch(multipartFile,language,httpServletRequest,currentTimeMillis,schneider);
        } catch (BizException e){
            return new Result(e.getCode(), e.getMessage());
        }  catch (Exception e) {
            log.error("图像搜索查询失败",e);
            return new Result<>(ResultCode.FAILED.getCode(),ResultCode.FAILED.getDesc());
        }
        return Result.buildSuccess(productVOS);
    }

    /**
     * 新增图像搜索实例照片
     * @param multipartFile
     * @return
     */
    public Result saveImageSearch(ProductReqData productReqData, MultipartFile multipartFile){
        try {
            if (multipartFile == null){
                return new Result(ResultCode.ILLEGAL_PARAM.getCode(),ResultCode.ILLEGAL_PARAM.getDesc());
            }
            AddImageResponse addImageResponse = productManager.saveImageSearch(productReqData, multipartFile);
            if (addImageResponse.getSuccess()){
                return Result.buildSuccess();
            }else {
                return Result.buildFailed();
            }
        } catch (BizException e){
            return new Result(e.getCode(), e.getMessage());
        }catch (Exception e) {
            log.error("新增图像搜索照片失败,入参productReqData:{}", JSON.toJSONString(productReqData),e);
            return new Result<>(ResultCode.FAILED.getCode(),ResultCode.FAILED.getDesc());
        }
    }

    /**
     * 查询日志
     * @return
     */
    public Result getLog(){
        ImageSearchLogVO imageSearchLogVO = null;
        try {
            imageSearchLogVO = imageSearchLogManager.getLog();
        }   catch (Exception e) {
            log.error("日志查询失败",e);
            return new Result<>(ResultCode.FAILED.getCode(),ResultCode.FAILED.getDesc());
        }
        return Result.buildSuccess(imageSearchLogVO);
    }

    /**
     * 模糊搜索产品
     * @param productReqData
     * @return
     */
    public Result listProductsFuzzySearch(ProductReqData productReqData){
        List<ProductVO> productVOS = null;
        try {
            productVOS = productManager.searchProductBySkuOrCategory(productReqData);
        } catch (BizException e){
            return new Result(e.getCode(),e.getMessage());
        } catch (Exception e) {
            log.error("产品模糊搜索失败",e);
            return new Result<>(ResultCode.FAILED.getCode(),ResultCode.FAILED.getDesc());
        }
        return Result.buildSuccess(productVOS);
    }
}
