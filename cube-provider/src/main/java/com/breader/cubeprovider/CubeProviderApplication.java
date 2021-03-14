package com.breader.cubeprovider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class CubeProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(CubeProviderApplication.class, args);
    }
}
