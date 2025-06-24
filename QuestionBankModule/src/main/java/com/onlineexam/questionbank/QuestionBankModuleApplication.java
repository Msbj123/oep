package com.onlineexam.questionbank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient; // Import for Eureka Client
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

/**
 * Main application class for the Question Bank Service.
 * @SpringBootApplication enables auto-configuration and component scanning.
 * @EnableDiscoveryClient registers this application with the Eureka Server.
 */
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)

//@EnableMethodSecurity
@EnableDiscoveryClient // Enables this Spring Boot application to act as a Eureka client
public class QuestionBankModuleApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuestionBankModuleApplication.class, args);
    }
}
