package com.schneider.imscore;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;

import javax.servlet.MultipartConfigElement;

@Slf4j
@SpringBootApplication
@MapperScan(basePackages = "com.schneider.imscore.mapper")
public class ImsCoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImsCoreApplication.class, args);
//        log.info("Start success");
    }


    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //单个文件最大
        factory.setMaxFileSize("10MB");
        return factory.createMultipartConfig();
    }
}
