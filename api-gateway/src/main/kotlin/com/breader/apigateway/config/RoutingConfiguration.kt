package com.breader.apigateway.config

import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RoutingConfiguration {

    @Bean
    fun routeLocatorConfig(locatorBuilder: RouteLocatorBuilder): RouteLocator = locatorBuilder.routes()
                .route { p -> p.path("/cube/**").uri("lb://cube-provider") }
                .build()

}
