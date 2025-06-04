package com.example.concurrentmap_l1_cache.l1cache;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ConcurrentMapL1Application {
    public static void main(String[] args) {
        SpringApplication.run(ConcurrentMapL1Application.class, args);
    }
}
