package org.example.config;

import org.example.websocket.NodeManagerWebSocketHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;

import java.util.Map;

/**
 * @author Frankie Chao
 */
@Configuration
@EnableWebFlux
public class WebFluxConfig {

    @Bean
    public HandlerMapping handlerMapping() {
        return new SimpleUrlHandlerMapping(Map.of("/nodeManagement", new NodeManagerWebSocketHandler()), -1);
    }
}
