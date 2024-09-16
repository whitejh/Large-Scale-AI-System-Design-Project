package com.team11.user_service.infrastructure.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@OpenAPIDefinition(
        info = @Info(title = "최강11 Service API 명세서",
                description = "최강11 Service API 명세서",
                version = "v1"))
@Configuration
public class SwaggerConfig {

    @Value("${server.swagger.host}")
    String host;

    @Value("${server.port}")
    String serverPort;

    @Value("${server.gateway.port}")
    String gatewayPort;

    @Bean
    public GroupedOpenApi publicAPI(){
        return GroupedOpenApi.builder()
                .group("com.team11.service")
                .pathsToMatch("/**")
                .build();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        List<Server> serverList = new ArrayList<>();
        Server server = new Server()
                .url("http://"+host+":"+serverPort+"/api")
                .description("Server");
        Server gatewayServer = new Server()
                .url("http://"+host+":"+gatewayPort+"/api")
                .description("Gateway Server");
        serverList.add(gatewayServer);
        serverList.add(server);
        return new OpenAPI()
                .servers(serverList)
                .components(new Components()
                        .addSecuritySchemes("JWT-Token", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER)
                                .name("Authorization")))
                .addSecurityItem(new SecurityRequirement().addList("JWT-Token"));
    }
}
