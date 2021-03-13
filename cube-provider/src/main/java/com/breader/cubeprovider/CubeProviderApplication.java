package com.breader.cubeprovider;

import com.breader.cubeprovider.model.Cube;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class CubeProviderApplication implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(CubeProviderApplication.class);

    @Value("${cube.size}")
    private int cubeSize;

    public static void main(String[] args) {
        SpringApplication.run(CubeProviderApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("cube.size={}", cubeSize);

        Cube c = new Cube(cubeSize, 'A');
        System.out.println(c);
    }
}
