/**
 * BEYONDSOFT.COM INC
 */
package com.schneider.imscore.biz.manager;

import com.schneider.imscore.mapper.SampleUserMapper;
import com.schneider.imscore.po.SampleUserPO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author yulijun
 * @version : SampleUserManager.java, v 0.1 2019/8/21 15:39 by Exp $$
 * @Description 业务逻辑
 */
@Slf4j
@Component
public class SampleUserManager {

    @Autowired
    private SampleUserMapper sampleUserMapper;

    public List<SampleUserPO> list(){
        log.info("{},{},{},{}",this.getClass().getCanonicalName(), this.getClass().getName(),
                this.getClass().getTypeName(), this.getClass().getSimpleName());
        return sampleUserMapper.list();
    }
}
