package com.github.diogodelima.gateway

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.cloud.gateway.route.builder.routes
import org.springframework.context.annotation.Bean

@SpringBootApplication
class GatewayApplication {

    @Bean
    fun customRouteLocator(builder: RouteLocatorBuilder): RouteLocator {
        return builder.routes()
            .route("tasks-service") { r ->
                r
                    .path("/tasks/**")
                    .uri("http://tasks-service:8080")
            }
            .build()
    }

}

fun main(args: Array<String>) {
    runApplication<GatewayApplication>(*args)
}
