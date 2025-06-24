package com.onlineexam.report.feignclient;

import com.onlineexam.report.dto.ResponseSummaryDTO; // Import the DTO from this service's DTOs package
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
// Removed @RequestHeader for Authorization, as Report Service no longer manages/sends JWT

import java.util.List;

@FeignClient(name = "onlineexam-response-service",url="http://localhost:8084/api/exam-management") // Name of the Response Microservice registered in Eureka
public interface ResponseFeignClient {

    // This method will call the Response Microservice's endpoint:
    // GET /api/responses/exam/{examId}/user/{userId}
    // Removed authorizationHeader parameter
    @GetMapping("exam/{examId}/user/{userId}")
    ResponseEntity<List<ResponseSummaryDTO>> getResponsesByUserAndExam(
            @PathVariable("userId") Integer userId,
            @PathVariable("examId") Integer examId
    );
}
