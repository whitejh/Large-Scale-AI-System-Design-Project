package com.team11.hub_service.infrastructure.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebCorsConfig implements WebMvcConfigurer {

    @Value("${server.swagger.host}")
    String host;

    @Value("${server.port}")
    String serverPort;

    @Value("${server.gateway.port}")
    String gatewayPort;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://" + host + ":" + serverPort,"http://" + host + ":" + gatewayPort)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                .allowedHeaders("*");
    }

}