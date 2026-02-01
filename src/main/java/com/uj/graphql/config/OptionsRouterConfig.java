package com.uj.graphql.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class OptionsRouterConfig {

    @Bean
    public RouterFunction<ServerResponse> optionsRouter() {
        return RouterFunctions.route()
                .OPTIONS("/graphql", request ->
                        ServerResponse.ok()
                                .header("Access-Control-Allow-Origin", "*")
                                .header("Access-Control-Allow-Methods", "POST, OPTIONS")
                                .header("Access-Control-Allow-Headers", "*")
                                .build()
                )
                .build();
    }
}

