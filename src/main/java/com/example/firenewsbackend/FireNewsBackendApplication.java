package com.example.firenewsbackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.firenewsbackend.mapper")
public class FireNewsBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(FireNewsBackendApplication.class, args);
    }

}
