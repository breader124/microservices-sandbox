package com.breader.apigateway.config

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.GlobalFilter
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class LoggingFilter : GlobalFilter {

    private val logger: Logger = LoggerFactory.getLogger(LoggingFilter::class.java)

    override fun filter(exchange: ServerWebExchange?, chain: GatewayFilterChain?): Mono<Void> {
        chain?.apply {
            exchange?.also {
                logger.info("request.id={}", it.request.id)
                logger.info("request.method={}", it.request.methodValue)
                return filter(exchange)
            }
        }
        return Mono.empty()
    }
}
