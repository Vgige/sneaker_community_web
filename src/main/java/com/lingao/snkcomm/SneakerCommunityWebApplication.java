package com.lingao.snkcomm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@MapperScan("com.lingao.snkcomm.mapper")
@SpringBootApplication
public class SneakerCommunityWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(SneakerCommunityWebApplication.class, args);
    }

}
