package com.ge;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.ge.common.mapper.BaseMapper;

/**
 * 在启动类中添加对mapper包扫描@MapperScan
 *
 */
@SpringBootApplication
@MapperScan(basePackages = "com.ge.**.mapper", markerInterface = BaseMapper.class)
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}