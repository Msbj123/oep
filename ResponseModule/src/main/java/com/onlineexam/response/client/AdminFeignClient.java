package com.onlineexam.response.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.onlineexam.response.DTO.ExamResponseDTO;

/**
 * Feign Client for Exam Management Service to communicate with Admin Service.
 * Used to fetch Exam definitions and check exam existence.
 */
@FeignClient(name = "admin-service", url = "${admin-service.url}") // 'lb://' for Eureka-based load balancing
public interface AdminFeignClient {

    /**
     * Retrieves a specific exam by its ID from the Admin Service.
     * @param id The ID of the exam.
     * @return ResponseEntity with ExamResponseDTO.
     */
    @GetMapping("/api/admin/exams/{id}") // Ensure this path matches the Admin Service's ExamController
    ResponseEntity<ExamResponseDTO> getExamById(@PathVariable("id") Integer id);

    /**
     * Checks if an exam exists in the Admin Service.
     * @param examId The ID of the exam to check.
     * @return ResponseEntity<Boolean> true if exists, false otherwise.
     */
    @GetMapping("/api/admin/exams/{examId}/exists") // Ensure this path matches the Admin Service's ExamController
    ResponseEntity<Boolean> examExists(@PathVariable("examId") Integer examId);
}
