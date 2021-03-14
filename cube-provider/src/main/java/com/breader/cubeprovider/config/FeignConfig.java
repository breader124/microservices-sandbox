package com.breader.cubeprovider.config;

import feign.codec.Encoder;
import feign.form.FormEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {
    @Bean
    public Encoder encoder() {
        return new FormEncoder();
    }
}
