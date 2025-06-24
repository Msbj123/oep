package com.onlineexam.response.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.onlineexam.response.client.AdminFeignClient;
import com.onlineexam.response.client.QuestionBankFeignClient;
import com.onlineexam.response.entity.ExamQuestionMapping;
import com.onlineexam.response.DTO.ExamQuestionMappingDTO;
import com.onlineexam.response.DTO.QuestionResponseDTO;
import com.onlineexam.response.Exception.ResourceNotFoundException;
import com.onlineexam.response.repository.ExamQuestionMappingRepo;

/**
 * Service class for managing Exam-Question mappings within the Exam Management Service.
 * This service is responsible for linking questions (from Question Bank) to exams (from Admin Service).
 */
@Service
public class ExamQuestionMappingService {

    @Autowired
    private ExamQuestionMappingRepo mappingRepository;

    @Autowired
    private AdminFeignClient adminFeignClient; // To validate exam existence in Admin Service

    @Autowired
    private QuestionBankFeignClient questionBankFeignClient; // To validate question existence in Question Bank Service

    /**
     * Saves a new mapping between an exam and a question.
     * Validates the existence of the exam and question using Feign clients.
     * Prevents duplicate mappings.
     *
     * @param examId The ID of the exam to link.
     * @param questionId The ID of the question to link.
     * @return The DTO of the newly created ExamQuestionMapping.
     * @throws ResourceNotFoundException If the exam or question does not exist in their respective services.
     * @throws IllegalArgumentException If the exact mapping already exists.
     */
    @Transactional
    public ExamQuestionMappingDTO saveMapping(Integer examId, Integer questionId) {
        // 1. Validate Exam Existence (call Admin Service)
        ResponseEntity<Boolean> examExistsResponse = adminFeignClient.examExists(examId);
        if (!examExistsResponse.getStatusCode().is2xxSuccessful() || !Boolean.TRUE.equals(examExistsResponse.getBody())) {
            throw new ResourceNotFoundException("Exam with ID " + examId + " not found in Admin Service. Cannot create mapping.");
        }

        // 2. Validate Question Existence (call Question Bank Service)
        // We call getQuestionById to ensure it's not just a boolean check, but the question actually exists and we can get details if needed.
        ResponseEntity<QuestionResponseDTO> questionResponse = questionBankFeignClient.getQuesById(questionId);
        if (!questionResponse.getStatusCode().is2xxSuccessful() || questionResponse.getBody() == null) {
            throw new ResourceNotFoundException("Question with ID " + questionId + " not found in Question Bank Service. Cannot create mapping.");
        }

        // 3. Check if mapping already exists to prevent duplicates
        if (mappingRepository.existsByExamIdAndQuestionId(examId, questionId)) {
            throw new IllegalArgumentException("Mapping for Exam ID " + examId + " and Question ID " + questionId + " already exists.");
        }

        // 4. Create and save the mapping
        ExamQuestionMapping mapping = new ExamQuestionMapping();
        mapping.setExamId(examId);
        mapping.setQuestionId(questionId);
        
        ExamQuestionMapping savedMapping = mappingRepository.save(mapping);
        return convertToDTO(savedMapping);
    }

    /**
     * Retrieves all mappings for a specific exam.
     * @param examId The ID of the exam.
     * @return A list of ExamQuestionMappingDTOs for the given exam.
     * @throws ResourceNotFoundException If the exam does not exist in Admin Service.
     */
    @Transactional(readOnly = true)
    public List<ExamQuestionMappingDTO> getMappingsByExam(Integer examId) {
        // Validate Exam Existence (call Admin Service)
        ResponseEntity<Boolean> examExistsResponse = adminFeignClient.examExists(examId);
        if (!examExistsResponse.getStatusCode().is2xxSuccessful() || !Boolean.TRUE.equals(examExistsResponse.getBody())) {
            throw new ResourceNotFoundException("Exam with ID " + examId + " not found in Admin Service. Cannot retrieve mappings.");
        }
        return mappingRepository.findByExamId(examId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a list of questions mapped to a specific exam, formatted as QuestionAttemptDTOs.
     * This method fetches the actual question details from the Question Bank Service for each mapped question.
     * The `correctAnswer` is omitted from `QuestionAttemptDTO` for security in a student-facing scenario.
     *
     * @param examId The ID of the exam for which to retrieve questions.
     * @return A list of QuestionAttemptDTOs that are part of the specified exam.
     * @throws ResourceNotFoundException If the exam does not exist.
     */
    @Transactional(readOnly = true)
    public List<QuestionResponseDTO> getAllQuestionsForExamAttempt(Integer examId) {
        // 1. Validate Exam Existence (call Admin Service)
        ResponseEntity<Boolean> examExistsResponse = adminFeignClient.examExists(examId);
        if (!examExistsResponse.getStatusCode().is2xxSuccessful() || !Boolean.TRUE.equals(examExistsResponse.getBody())) {
            throw new ResourceNotFoundException("Exam with ID " + examId + " not found in Admin Service. Cannot prepare exam attempt.");
        }

        // 2. Get all mapping entries for this exam from local database
        List<ExamQuestionMapping> mappings = mappingRepository.findByExamId(examId);
        if (mappings.isEmpty()) {
            return List.of(); // No questions mapped to this exam, return empty list
        }

        // 3. Extract question IDs from mappings and fetch full question details from Question Bank Service
        // This is done one-by-one, a bulk endpoint in QuestionBankService would be more efficient for large lists.
        List<QuestionResponseDTO> questionsForAttempt = new java.util.ArrayList<>();
        for (ExamQuestionMapping mapping : mappings) {
            Integer questionId = mapping.getQuestionId();
            ResponseEntity<QuestionResponseDTO> questionResponse = questionBankFeignClient.getQuesById(questionId);

            if (questionResponse.getStatusCode().is2xxSuccessful() && questionResponse.getBody() != null) {
                QuestionResponseDTO qbQuestion = questionResponse.getBody();
                // Map to QuestionAttemptDTO, omitting correct answer
                questionsForAttempt.add(new QuestionResponseDTO(
                    qbQuestion.getQuestionId(),
                    qbQuestion.getText(),
                    qbQuestion.getCategory(),
                    qbQuestion.getDifficulty(),
                    qbQuestion.getOption1(),
                    qbQuestion.getOption2()
                    		// This examId from QB Service indicates which exam it "belongs" to conceptually
                ));
            } else {
                System.err.println("Warning: Could not retrieve details for mapped question ID " + questionId + " from Question Bank Service. Skipping.");
                // Depending on requirements, you might want to log this as an error or throw a more specific exception.
            }
        }
        return questionsForAttempt;
    }


    /**
     * Retrieves all existing exam-question mappings from the database.
     * @return A list of all ExamQuestionMappingDTOs.
     */
    @Transactional(readOnly = true)
    public List<ExamQuestionMappingDTO> getAllMappings() {
        return mappingRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Deletes a specific exam-question mapping by its ID.
     * @param mappingId The ID of the mapping to delete.
     * @return true if the mapping was successfully deleted.
     * @throws ResourceNotFoundException If the mapping with the given ID does not exist.
     */
    @Transactional
    public boolean deleteMapping(Long mappingId) {
        if (!mappingRepository.existsById(mappingId)) {
            throw new ResourceNotFoundException("Exam-Question mapping with ID " + mappingId + " not found. Cannot delete.");
        }
        mappingRepository.deleteById(mappingId);
        return true;
    }

    // Helper method to convert ExamQuestionMapping entity to ExamQuestionMappingDTO
    /*
    private ExamQuestionMappingDTO convertToDTO(ExamQuestionMapping mapping) {
        return new ExamQuestionMappingDTO(
                mapping.getId(),
                mapping.getExamId(),
                mapping.getQuestionId()
        );
        */
    public ExamQuestionMappingDTO convertToDTO(ExamQuestionMapping eqm) {
		return new ExamQuestionMappingDTO(
				eqm.getExamId(),
				eqm.getQuestionId()
			
			);
    }

	public ResponseEntity<String> deleteMappingsByExamId(Integer examId) {
		// TODO Auto-generated method stub
		 List<ExamQuestionMapping> mappings = mappingRepository.findByExamId(examId);
	        if (mappings.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No mappings found for Exam ID: " + examId);
	        }
	        mappingRepository.deleteAll(mappings);
	        return ResponseEntity.ok("Mappings deleted for Exam ID: " + examId);
	}
}
