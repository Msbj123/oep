package com.onlineexam.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate; // NEW IMPORT
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Main application class for the Admin Service.
 * This class uses @SpringBootApplication to enable auto-configuration,
 * component scanning, and define this as a Spring Boot application.
 * @EnableFeignClients enables the scanning for Feign client interfaces.
 */
@SpringBootApplication
@EnableEurekaServer
@EnableFeignClients(basePackages = "com.onlineexam.admin.client") // Specifies where to look for Feign client interfaces
public class AdminModuleApplication {

    public static void main(String[] args) {
        // Entry point for the Spring Boot application.
        SpringApplication.run(AdminModuleApplication 
.class, args);
    }

    /**
     * Provides a RestTemplate bean. This is needed by TokenProvider for its internal
     * call to the User Service's token endpoint.
     * @return A configured RestTemplate instance.
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

