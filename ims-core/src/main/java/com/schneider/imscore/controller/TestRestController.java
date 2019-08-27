/**
 * BEYONDSOFT.COM INC
 */
package com.schneider.imscore.controller;

import com.schneider.imscore.biz.manager.SampleUserManager;
import com.schneider.imscore.biz.service.SampleUserService;
import com.schneider.imscore.resp.Result;
import com.schneider.imscore.resp.ResultCode;
import com.schneider.imscore.resp.exception.BizException;
import com.schneider.imscore.vo.SampleUserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author yulijun
 * @version : TestRestController.java, v 0.1 2019/8/21 15:09 by Exp $$
 * @Description demo
 */
@RestController
public class TestRestController {

    @Autowired
    private SampleUserService sampleUserService;

    @GetMapping("/hello")
    public String hello(){
        return "hello word!";
    }

    @GetMapping("/user")
    public Result<List<SampleUserVO>> user(){
        return sampleUserService.list();
    }


    @GetMapping("/test")
    public Result<List<SampleUserVO>> error(){
        throw new BizException(ResultCode.FAILED);
    }

}
