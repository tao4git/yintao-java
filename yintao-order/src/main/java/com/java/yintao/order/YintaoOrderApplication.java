package com.java.yintao.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.java.yintao.order.mapper")
public class YintaoOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(YintaoOrderApplication.class, args);
    }

}
