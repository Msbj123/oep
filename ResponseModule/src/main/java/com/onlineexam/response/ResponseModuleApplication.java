package com.onlineexam.response;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient; // Enables Eureka client
import org.springframework.cloud.openfeign.EnableFeignClients; // Enables Feign clients

/**
 * Main application class for the Exam Management Service.
 * @SpringBootApplication enables auto-configuration and component scanning.
 * @EnableDiscoveryClient registers this application with the Eureka Server.
 * @EnableFeignClients enables scanning for Feign client interfaces in the specified package.
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.onlineexam.response.client") // Scan for Feign clients in this package
public class ResponseModuleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ResponseModuleApplication.class, args);
    }
}
