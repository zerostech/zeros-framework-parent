package com.zerostech.service;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Application
 * 这个类暂时没有任何用处
 * @author hzzjb
 * @date 2017/4/21
 */
@EnableDiscoveryClient
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
