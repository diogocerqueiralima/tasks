package com.github.diogodelima.gateway

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.cloud.gateway.route.builder.routes
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpHeaders
import org.springframework.web.reactive.function.client.WebClient

@SpringBootApplication
class GatewayApplication(

    private val webClient: WebClient

    ) {

    @Bean
    fun customRouteLocator(builder: RouteLocatorBuilder): RouteLocator {
        return builder.routes()
            .route("tasks-service") { r ->
                r
                    .path("/tasks/**", "/steps/**")
                    .filters { f ->
                        f.filter(userInfoFilter())
                    }
                    .uri("http://tasks-service:8080")
            }
            .build()
    }

    fun userInfoFilter() =

        GatewayFilter { exchange, chain ->

            val token = exchange.request.headers.getFirst(HttpHeaders.AUTHORIZATION)?.substring(7)

            if (token != null) {

                webClient.get()
                    .uri("http://authorization-server:9000/userinfo")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer $token")
                    .retrieve()
                    .bodyToMono(Map::class.java)
                    .flatMap { body ->

                        val userId = body["user_id"] as Int
                        val modifiedExchange = exchange.mutate()
                            .request { req ->
                                req.header("User-Id", userId.toString())
                            }
                            .build()

                        chain.filter(modifiedExchange)
                    }

            }else chain.filter(exchange)

        }

}

fun main(args: Array<String>) {
    runApplication<GatewayApplication>(*args)
}
