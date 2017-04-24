package com.zerostech.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;

/**
 * Application
 *
 * @author hzzjb
 */
@SpringBootApplication
@ComponentScan("com.zerostech.server.controller")
@MapperScan("com.zerostech.wechat.dao")
public class Application {

    public static void main(String[] args) {
        new SpringApplicationBuilder(Application.class).web(true).run(args);
    }
}
