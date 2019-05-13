package com.yoge.ad.service.dumps;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients
public class DumpsApplication {

    public static void main(String[] args) {
        SpringApplication.run(DumpsApplication.class, args);
    }

}
