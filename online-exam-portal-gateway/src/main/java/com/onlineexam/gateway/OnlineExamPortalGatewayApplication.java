package com.onlineexam.gateway;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient // Enables Eureka service discovery
public class OnlineExamPortalGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(OnlineExamPortalGatewayApplication.class, args);
    }
}
