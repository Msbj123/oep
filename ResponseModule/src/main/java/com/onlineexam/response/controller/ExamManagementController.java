package com.onlineexam.response.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
// import org.springframework.security.access.prepost.PreAuthorize; // Add Spring Security annotations here if needed
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.onlineexam.response.client.AdminFeignClient; // To call Admin Service
import com.onlineexam.response.client.QuestionBankFeignClient; // To call Question Bank Service
import com.onlineexam.response.client.UserFeignClient; // To call User Service
import com.onlineexam.response.entity.Response; // Local Response entity
import com.onlineexam.response.repository.ResponseRepository;
import com.onlineexam.response.DTO.ExamResponseDTO;
import com.onlineexam.response.DTO.ExamSubmissionDTO;
import com.onlineexam.response.DTO.QuestionResponseDTO;
import com.onlineexam.response.DTO.ResponseSummaryDTO;
import com.onlineexam.response.DTO.UserDTO;
import com.onlineexam.response.Exception.ResourceNotFoundException; // Local exception
import com.onlineexam.response.service.ExamQuestionMappingService; // To get mapped questions for attempt
import com.onlineexam.response.service.ResponseService; // To process submissions and fetch responses/results

/**
 * REST Controller for managing Exam Attempts, Submissions, and viewing Results.
 * This controller resides in the Exam Management Service.
 */
@RestController
@RequestMapping("/api/exam-management") // Base path for Exam Attempt/Results endpoints in this service
public class ExamManagementController {

    @Autowired
    private AdminFeignClient adminFeignClient; // To get exam details from Admin Service

    @Autowired
    private QuestionBankFeignClient questionBankFeignClient; // To get questions (full details for scoring, or just to check existence)

    @Autowired
    private UserFeignClient userFeignClient; // To get user details for reports/validation

    @Autowired
    private ResponseService responseService; // To handle exam submissions and fetch responses

    @Autowired
    private ExamQuestionMappingService examQuestionMappingService; // To get questions linked to an exam via mapping

    @Autowired
    private ResponseRepository responseRepository;
    /**
     * Student attempts an exam: Fetches exam details and all mapped questions for a given exam.
     * This endpoint retrieves information necessary for a student to start an exam.
     * It uses ExamQuestionMappingService to get questions that are explicitly linked to this exam.
     *
     * @param examId The ID of the exam to attempt.
     * @return ResponseEntity with a Map containing 'examDetails' (ExamResponseDTO) and 'questions' (List of QuestionAttemptDTOs).
     * @throws ResourceNotFoundException if the exam is not found in Admin Service.
     */
    @GetMapping("/{examId}/attempt")
    // @PreAuthorize("hasRole('STUDENT')") // Example: Restrict to students
    public ResponseEntity<Map<String, Object>> getExamDetailsAndQuestionsForAttempt(@PathVariable Integer examId) {
        // 1. Fetch Exam Details from Admin Service
        ResponseEntity<ExamResponseDTO> examDetailsResponse = adminFeignClient.getExamById(examId);
        if (!examDetailsResponse.getStatusCode().is2xxSuccessful() || examDetailsResponse.getBody() == null) {
            throw new ResourceNotFoundException("Exam with ID " + examId + " not found in Admin Service.");
        }
        ExamResponseDTO examDetails = examDetailsResponse.getBody();

        // 2. Fetch mapped questions from ExamQuestionMappingService.
        // This service will internally call the QuestionBankFeignClient to get the question details.
        List<QuestionResponseDTO> questionsForAttempt = examQuestionMappingService.getAllQuestionsForExamAttempt(examId);
        
        // Assemble response for the client (frontend).
        // This structure allows the frontend to easily display both exam metadata and the questions.
        Map<String, Object> responseMap = new java.util.HashMap<>();
        responseMap.put("examDetails", examDetails);
        responseMap.put("questions", questionsForAttempt); // Now uses QuestionAttemptDTO (no correct answer)

        return ResponseEntity.ok(responseMap);
    }

    /**
     * Handles student exam submissions.
     * This endpoint receives a student's submitted answers, calculates marks, and persists responses.
     *
     * @param examId The ID of the exam being submitted.
     * @param submissionDTO Contains the user's ID and their answers for each question.
     * @return ResponseEntity with a summary of the responses and marks obtained (List of ResponseSummaryDTOs).
     */
    @PostMapping("/{examId}/submit")
    // @PreAuthorize("hasRole('STUDENT')") // Example: Restrict to students
    public ResponseEntity<List<ResponseSummaryDTO>> submitExam(@PathVariable Integer examId, @RequestBody ExamSubmissionDTO submissionDTO) {
    	ResponseEntity<List<ResponseSummaryDTO>> responseEntity = responseService.submitExam(examId, submissionDTO);
        List<ResponseSummaryDTO> results = responseEntity.getBody(); // extract the list safely
        return ResponseEntity.ok(results);
    }

    /**
     * Admin view: Get all responses for a specific exam, including detailed user and question info.
     * This endpoint aggregates data from ResponseService, User Service, and Question Bank Service.
     *
     * @param examId The ID of the exam.
     * @return ResponseEntity with a list of maps, each containing response, user, and question details.
     */
    @GetMapping("/{examId}/responses")
    // @PreAuthorize("hasRole('ADMIN')") // Example: Restrict to admins
    public ResponseEntity<List<Map<String, Object>>> getResponsesByExamIdDetailed(@PathVariable Integer examId) {
        List<Response> responses = responseService.getResponsesByExam(examId);
        List<Map<String, Object>> detailedResponses = responses.stream().map(response -> {
            Map<String, Object> item = new java.util.HashMap<>();
            item.put("response", response);

            // Fetch user details from User Service
            try {
                ResponseEntity<UserDTO> userRes = userFeignClient.getUserProfile(response.getUserId());
                if (userRes.getStatusCode().is2xxSuccessful() && userRes.getBody() != null) {
                    item.put("user", userRes.getBody());
                } else {
                    item.put("user", "User details not available");
                }
            } catch (Exception e) {
                item.put("user", "Error fetching user details: " + e.getMessage());
                System.err.println("Error fetching user " + response.getUserId() + ": " + e.getMessage());
            }

            // Fetch question details from Question Bank Service
            try {
                ResponseEntity<QuestionResponseDTO> questionRes = questionBankFeignClient.getQuesById(response.getQuestionId());
                if (questionRes.getStatusCode().is2xxSuccessful() && questionRes.getBody() != null) {
                    item.put("question", questionRes.getBody());
                } else {
                    item.put("question", "Question details not available");
                }
            } catch (Exception e) {
                item.put("question", "Error fetching question details: " + e.getMessage());
                System.err.println("Error fetching question " + response.getQuestionId() + ": " + e.getMessage());
            }
            return item;
        }).collect(Collectors.toList());

        return new ResponseEntity<>(detailedResponses, HttpStatus.OK);
    }


    /**
     * Admin/Student view: Get responses of a specific user for a specific exam.
     *
     * @param examId The ID of the exam.
     * @param userId The ID of the user.
     * @return ResponseEntity with a list of ResponseSummaryDTOs containing details.
     */
    @GetMapping("/{examId}/responses/user/{userId}")
    // @PreAuthorize("hasRole('ADMIN') or (hasRole('STUDENT') and #userId == authentication.principal.id)") // Example: Student can only see their own
    public ResponseEntity<List<ResponseSummaryDTO>> getUserResponsesForExam(@PathVariable Integer examId, @PathVariable Integer userId) {
        List<Response> responses = responseService.getResponsesByUserAndExam(userId, examId);
        List<ResponseSummaryDTO> responseSummaryDTOs = responses.stream()
                .map(r -> new ResponseSummaryDTO(
                        r.getResponseId(),
                        r.getQuestionId(),
                        r.getSubmittedAnswer(),
                        r.getMarksObtained()
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseSummaryDTOs);
    }
    
    @GetMapping("/exam/{examId}/user/{userId}")
    public List<Response> getResponsesByUserAndExam(@PathVariable Integer userId,@PathVariable Integer examId) {
        // Validate exam existence using AdminFeignClient
        ResponseEntity<ExamResponseDTO> examResponse = adminFeignClient.getExamById(examId);
        if (!examResponse.getStatusCode().equals(HttpStatus.OK) || examResponse.getBody() == null) {
            throw new ResourceNotFoundException("Exam not found with ID: " + examId);
        }
        List<Response> responses = responseRepository.findByUserIdAndExamId(userId, examId);
        if (responses.isEmpty()) {
            throw new ResourceNotFoundException("No responses found for user ID: " + userId + " and exam ID: " + examId);
        }
        return responses;
    }

    
}
