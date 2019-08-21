/**
 * BEYONDSOFT.COM INC
 */
package com.schneider.imscore.biz.service;

import com.schneider.imscore.biz.manager.SampleUserManager;
import com.schneider.imscore.resp.Result;
import com.schneider.imscore.util.BeanConvertUtils;
import com.schneider.imscore.vo.SampleUserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yulijun
 * @version : SampleUserService.java, v 0.1 2019/8/21 15:40 by Exp $$
 * @Description 参数校验
 */
@Service
@Slf4j
public class SampleUserService {

    @Autowired
    private SampleUserManager sampleUserManager;

    public Result<List<SampleUserVO>> list(){
        log.info("{},{},{},{}",this.getClass().getCanonicalName(), this.getClass().getName(),
                this.getClass().getTypeName(), this.getClass().getSimpleName());
        return Result.buildSuccess(BeanConvertUtils.convertList(sampleUserManager.list(), SampleUserVO.class));
    }
}
