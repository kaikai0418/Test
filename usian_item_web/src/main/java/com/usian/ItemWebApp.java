package com.usian;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/*@SpringBootApplication
@EnableDiscoveryClient  */// 等价于@EnableEurekaClient
@EnableFeignClients   // 开启feign功能
@SpringCloudApplication
public class ItemWebApp {
    public static void main(String[] args) {
        SpringApplication.run(ItemWebApp.class, args);
    }
}
