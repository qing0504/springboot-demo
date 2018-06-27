package com.springboot.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApplicationMain {  
    public static void main(String[] args) {
        //启动Spring Boot项目的唯一入口  
        SpringApplication.run(ApplicationMain.class, args);
    }
  
} 
