package com.onlineexam.response.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.onlineexam.response.DTO.QuestionDTO;
import com.onlineexam.response.DTO.QuestionResponseDTO;

import java.util.List;

/**
 * Feign Client for Exam Management Service to communicate with Question Bank Service.
 * Used to fetch question details (e.g., text, correct answer) for exam attempts and scoring.
 */
@FeignClient(name = "questionbank-service", url = "${questionbank-service.url}")
public interface QuestionBankFeignClient {

    /**
     * Retrieves all questions associated with a specific exam ID from the Question Bank Service.
     * This is useful for fetching all questions that *could* be part of an exam.
     * @param examId The ID of the exam.
     * @return ResponseEntity with a list of QuestionResponseDTO.
     */
    @GetMapping("/api/questionbank/questions/exam/{examId}") // Ensure this path matches the QuestionBank Service's QuestionController
    ResponseEntity<List<QuestionResponseDTO>> getQuestionsByExamId(@PathVariable("examId") Integer examId);

    /**
     * Retrieves a specific question by its ID from the Question Bank Service.
     * This is crucial for getting individual question details for scoring or validation.
     * @param id The ID of the question.
     * @return ResponseEntity with QuestionResponseDTO.ew
     */
   // @GetMapping("/api/questionbank/exams/{examId}/questions/{id}") // Ensure this path matches the QuestionBank Service's QuestionController
    //QuestionDTO getQuestionById(@PathVariable("examId") Integer examId,@PathVariable("id") Integer id);
    
    @GetMapping("/api/questionbank/questions/{id}") // Ensure this path matches the QuestionBank Service's QuestionController
    QuestionDTO QuestionById(@PathVariable("id") Integer id);
    
    @GetMapping("/api/questionbank/exams/{examId}/questions/{id}") // Ensure this path matches the QuestionBank Service's QuestionController
    ResponseEntity<QuestionResponseDTO> getQuestionById(@PathVariable("examId") Integer examId,@PathVariable("id") Integer id);

    @GetMapping("/api/questionbank/questionById/{id}")
	ResponseEntity<QuestionResponseDTO> getQuesById(@PathVariable("id") Integer questionId);
}
