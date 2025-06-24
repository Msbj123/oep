package com.onlineexam.report;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableDiscoveryClient // Enables this service to register with Eureka
@EnableFeignClients(basePackages = "com.onlineexam.report.feignclient") // Enable Feign Clients and specify base package
@EntityScan(basePackages = {"com.onlineexam.report.entity"}) // Scan entities in this package
@EnableJpaRepositories(basePackages = {"com.onlineexam.report.repository"}) // Scan repositories in this package
public class OnlineexamReportServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineexamReportServiceApplication.class, args);
    }
}
