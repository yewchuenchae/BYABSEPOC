package com.schneider.imscore.biz.manager.log;

import com.schneider.imscore.mapper.product.ImageSearchLogMapper;
import com.schneider.imscore.vo.log.ImageSearchLogVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author liyuan
 * @createDate 2019/09/16 16:42
 * @Description
 */
@Component
public class ImageSearchLogManager {
    @Autowired
    private ImageSearchLogMapper imageSearchLogMapper;

    /**
     * 日志信息
     * @return
     */
    public ImageSearchLogVO getLog(){
        ImageSearchLogVO imageSearchLogVO = imageSearchLogMapper.selectImageSearchLog();
        if (imageSearchLogVO != null){
            Long wholeApiTime = imageSearchLogVO.getWholeApiTime();
            Long requestCount = imageSearchLogVO.getRequestCount();
            String average = String.format("%.2f", ((wholeApiTime.doubleValue() / requestCount.doubleValue()/1000)));
            imageSearchLogVO.setAverageApiTime(average);
        }
        return imageSearchLogVO;
    }
}
