package com.wtao.healthy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.wtao.healthy.mapper")
@EnableScheduling
public class HealthyApplication {

    public static void main(String[] args) {
        SpringApplication.run(HealthyApplication.class, args);
    }

}
