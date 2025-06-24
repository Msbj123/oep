package com.onlineexam.admin.client;

import com.onlineexam.admin.dto.IdResponseDTO; // Re-use the existing IdResponseDTO
import com.onlineexam.admin.dto.QuestionDTO;
import com.onlineexam.admin.dto.QuestionResponseDTO;

//import com.onlineexam.admin.dto.QuestionDTO; // Re-use the existing QuestionDTO (from admin's perspective)
//import com.onlineexam.admin.dto.QuestionResponseDTO; // Re-use the existing QuestionResponseDTO (from admin's perspective)
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Feign Client interface for interacting with the Question Bank Service.
 * This client will be used by the Admin Service to manage questions.
 * It uses 'lb://questionbank-service' for Eureka-based load balancing.
 */
@FeignClient(name = "questionbank-service", url = "lb://questionbank-service") // No specific config needed if no custom headers like Auth
public interface QuestionBankFeignClient {

    // Endpoints correspond to the Question Bank Service's expected API.
    // Note: The original QuestionController had /api/admin/questions and /api/admin/questions/exam/{examId}
    // We're making a direct mapping here assuming QuestionBank will own these routes.

    /**
     * Creates a new question and associates it with an exam ID in the Question Bank Service.
     * The Question Bank Service will handle validating the exam ID.
     * @param examId The ID of the exam to associate the question with.
     * @param questionDTO The question data.
     * @return ResponseEntity with IdResponseDTO.
     */
    @PostMapping("/api/questionbank/questions/exam/{examId}")
    ResponseEntity<IdResponseDTO> createQuestion(@PathVariable("examId") Integer examId, @RequestBody QuestionDTO questionDTO);

    /**
     * Retrieves all questions from the Question Bank Service.
     * @return ResponseEntity with a list of QuestionResponseDTO.
     */
    @GetMapping("/api/questionbank/questions")
    ResponseEntity<List<QuestionResponseDTO>> getAllQuestions();

    /**
     * Retrieves a specific question by its ID from the Question Bank Service.
     * @param id The ID of the question.
     * @return ResponseEntity with QuestionResponseDTO.
     */
    @GetMapping("/api/questionbank/questions/{id}")
    ResponseEntity<QuestionResponseDTO> getQuestionById(@PathVariable("id") Integer id);

    /**
     * Retrieves questions associated with a specific exam ID from the Question Bank Service.
     * @param examId The ID of the exam.
     * @return ResponseEntity with a list of QuestionResponseDTO.
     */
    @GetMapping("/api/questionbank/questions/exam/{examId}")
    ResponseEntity<List<QuestionResponseDTO>> getQuestionsByExamId(@PathVariable("examId") Integer examId);

    /**
     * Updates an existing question in the Question Bank Service.
     * @param id The ID of the question to update.
     * @param questionDTO The updated question data.
     * @return ResponseEntity with QuestionResponseDTO.
     */
    @PutMapping("/api/questionbank/questions/{id}")
    ResponseEntity<QuestionResponseDTO> updateQuestion(@PathVariable("id") Integer id, @RequestBody QuestionDTO questionDTO);

    /**
     * Deletes a question from the Question Bank Service.
     * @param id The ID of the question to delete.
     * @return ResponseEntity<Void>.
     */
    @DeleteMapping("/api/questionbank/questions/{id}")
    ResponseEntity<Void> deleteQuestion(@PathVariable("id") Integer id);
}
