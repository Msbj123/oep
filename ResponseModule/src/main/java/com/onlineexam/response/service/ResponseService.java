package com.onlineexam.response.service;


import com.onlineexam.response.client.AdminFeignClient; // To call Admin Service for Exam details
import com.onlineexam.response.client.QuestionBankFeignClient; // To call Question Bank Service for Question details
import com.onlineexam.response.client.UserFeignClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.onlineexam.response.DTO.AnswerSubmissionDTO;
import com.onlineexam.response.DTO.ExamResponseDTO;
import com.onlineexam.response.DTO.ExamSubmissionDTO;
import com.onlineexam.response.DTO.QuestionResponseDTO;
import com.onlineexam.response.DTO.QuestionDTO;
import com.onlineexam.response.DTO.ResponseSummaryDTO;
import com.onlineexam.response.DTO.UserDTO;
import com.onlineexam.response.entity.Response;
import com.onlineexam.response.Exception.ResourceNotFoundException;
//import com.onlineexam.response.repository.ExamRepository;
//import com.onlineexam.demo.repository.QuestionBankRepo;
import com.onlineexam.response.repository.ResponseRepository;
//import com.example.demo.Repository.UserRepository;

import feign.FeignException;

@Service
public class ResponseService {
	
    //@Autowired
    //private ExamRepository examRepository;
    
   // @Autowired
    //private QuestionBankRepo qbRepo;
    @Autowired
    private ResponseRepository responseRepository;
   // @Autowired
    //private UserRepository userRepo;
   
	 	@Autowired
	    private AdminFeignClient adminFeignClient; // To get exam details from Admin Service

	    @Autowired
	    private QuestionBankFeignClient questionBankFeignClient;
	    
	    // To get questions and their 

	    @Autowired
	    private UserFeignClient userFeignClient;
	    
    public Response submitResponse(Response response) {
//    	if(userRepo.existsById(response.getUserId()))
    	//if(userFeignClient.getUserProfile(response.getUserId()))
    	if(userFeignClient.getUserProfile(response.getUserId()) != null)
    	{	
        return responseRepository.save(response);
    	}
    	else {
    		throw new ResourceNotFoundException("No user found with "+response.getUserId());
    	}
    }

    public List<Response> getResponsesByUser(Integer userId) {
    	List<Response> responses = responseRepository.findByUserId(userId);
    		if (responses.isEmpty()) {
    			throw new ResourceNotFoundException("No responses found for user ID: " + userId);
    		}
        return responseRepository.findByUserId(userId);
    }
    /*
    public List<Response> getResponsesByExam(Integer examId) {
        if (!examRepository.existsById(examId)) {
            throw new ResourceNotFoundException("Exam not found with ID: " + examId);
        }
        return responseRepository.findByExamId(examId);
    }
    */
    public List<Response> getResponsesByExam(Integer examId) {
        // Validate exam existence using AdminFeignClient
        ResponseEntity<ExamResponseDTO> examResponse = adminFeignClient.getExamById(examId);
        if (!examResponse.getStatusCode().equals(HttpStatus.OK) || examResponse.getBody() == null) {
            throw new ResourceNotFoundException("Exam not found with ID: " + examId);
        }
        List<Response> responses = responseRepository.findByExamId(examId);
        if (responses.isEmpty()) {
            throw new ResourceNotFoundException("No responses found for exam ID: " + examId);
        }
        return responses;
    }
/*
    public List<Response> getResponsesByUserAndExam(Integer userId, Integer examId) {
        if (!examRepository.existsById(examId)) {
            throw new ResourceNotFoundException("Exam not found with ID: " + examId);
        }
        return responseRepository.findByUserIdAndExamId(userId, examId);
    }
*/
    public List<Response> getResponsesByUserAndExam(Integer userId, Integer examId) {
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
    /*
	public ResponseEntity<List<ResponseSummaryDTO>> submitExam(Integer examId, ExamSubmissionDTO submissionDTO) {
		// TODO Auto-generated method stub
		List<ResponseSummaryDTO> responseSummaries = new ArrayList<>();
        Optional<Exam> examOpt = examRepository.findById(examId);
        		

        if (!examOpt.isPresent()) {
            throw new ResourceNotFoundException("Exam not found with ID: " + examId);
        }

        Exam exam = examOpt.get();

        for (AnswerSubmissionDTO ans : submissionDTO.getAnswers()) {
            Optional<QuestionBank> questionOpt = qbRepo.findById(ans.getQuestionId());
            		
            if (!questionOpt.isPresent()) {
                throw new ResourceNotFoundException("Question not found with ID: " + ans.getQuestionId());
            }

            QuestionBank question = questionOpt.get();
           
            
            Response response = new Response();
            response.setUserId(submissionDTO.getUserId());
            response.setExam(exam);
            response.setQuestion(question);
            response.setSubmittedAnswer(ans.getSubmittedAnswer());

           int marks = question.getCorrectAnswer().equals(ans.getSubmittedAnswer()) ? 2 : 0;
            response.setMarksObtained(marks);
            Response savedResponse = submitResponse(response);

            ResponseSummaryDTO summary = new ResponseSummaryDTO(
                    savedResponse.getResponseId(),
                    question.getQuestionId(),
                    ans.getSubmittedAnswer(),
                    marks
            );
            responseSummaries.add(summary);
        }

        return ResponseEntity.ok(responseSummaries);
	}
	*/
    /*
    public ResponseEntity<List<ResponseSummaryDTO>> submitExam(Integer examId, ExamSubmissionDTO submissionDTO) {
        List<ResponseSummaryDTO> responseSummaries = new ArrayList<>();

        // Fetch exam details from Admin Service
        ResponseEntity<ExamResponseDTO> examResponse = adminFeignClient.getExamById(examId);
        if (!examResponse.getStatusCode().equals(HttpStatus.OK) || examResponse.getBody() == null) {
            throw new ResourceNotFoundException("Exam not found with ID: " + examId);
        }
        ExamResponseDTO exam = examResponse.getBody(); // Use the DTO received from Feign client

        for (AnswerSubmissionDTO ans : submissionDTO.getAnswers()) {
            // Fetch question details from Question Bank Service
            ResponseEntity<QuestionResponseDTO> questionResponse = questionBankFeignClient.getQuesById(ans.getQuestionId());
            if (!questionResponse.getStatusCode().equals(HttpStatus.OK) || questionResponse.getBody() == null) {
                throw new ResourceNotFoundException("Question not found with ID: " + ans.getQuestionId());
            }
            
            // Use the DTO received from Feign client

            Response response = new Response();
            response.setUserId(submissionDTO.getUserId());
            response.setExamId(examId);
            response.setQuestionId(ans.getQuestionId());
            QuestionDTO q=questionBankFeignClient.QuestionById(ans.getQuestionId());
            
            // Let's assume for `response.setExam(exam);` that `response.setExam` accepts an `Exam` entity.
            // I'll create a minimal `Exam` entity class within the same package for compilation.
            //com.onlineexam.response.entity.Exam examEntity = new com.onlineexam.response.entity.Exam();
            
//            examEntity.setExamId(exam.getExamId());
//            response.setExam(examEntity);

            // Similar assumption for Question.
            //com.onlineexam.response.entity.QuestionBank questionEntity = new com.onlineexam.response.entity.QuestionBank();
            //questionEntity.setQuestionId(question.getQuestionId());
           // response.setCorrectAnswer(q.getCorrectAnswer()); // Needed for marks calculation
           // response.setQuestion(questionEntity);

            response.setSubmittedAnswer(ans.getSubmittedAnswer());

            // Calculate marks
            int marks = 0;
            if (q.getCorrectAnswer() != null && ans.getSubmittedAnswer() != null &&
                q.getCorrectAnswer().equalsIgnoreCase(ans.getSubmittedAnswer())) {
                marks = 2; // Assuming 2 marks for correct answer
            }
            response.setMarksObtained(marks);

            // Submit and save the response
            Response savedResponse = submitResponse(response);

            // Create response summary DTO
            ResponseSummaryDTO summary = new ResponseSummaryDTO(
                    savedResponse.getResponseId(),
                    q.getQuestionId(),
                    ans.getSubmittedAnswer(),
                    marks
            );
            responseSummaries.add(summary);
        }

        return ResponseEntity.ok(responseSummaries);
    }*/
    public ResponseEntity<List<ResponseSummaryDTO>> submitExam(Integer examId, ExamSubmissionDTO submissionDTO) throws FeignException{
        List<ResponseSummaryDTO> responseSummaries = new ArrayList<>();
        Integer id=submissionDTO.getUserId();
        /*
        // Fetch exam details from Admin Service
        ResponseEntity<ExamResponseDTO> examResponse = adminFeignClient.getExamById(examId);
        if (!examResponse.getStatusCode().equals(HttpStatus.OK) || examResponse.getBody() == null) {
            throw new ResourceNotFoundException("Exam not found with ID: " + examId);
        }
        */
        List<Response> existingResponses = responseRepository.findByUserIdAndExamId(id, examId);
        if (!existingResponses.isEmpty()) {
            throw new IllegalStateException("User has already submitted responses for this exam. Re-attempt not allowed.");
        }
        try {
            ResponseEntity<ExamResponseDTO> examResponse = adminFeignClient.getExamById(examId);
            if (!examResponse.getStatusCode().equals(HttpStatus.OK) || examResponse.getBody() == null) {
                throw new ResourceNotFoundException("Exam not found with ID: " + examId);
            }
        } catch (FeignException.NotFound e) {
            throw new ResourceNotFoundException("Exam not found with ID: " + examId);
        }

        try {
            //ResponseEntity<ExamResponseDTO> examResponse = adminFeignClient.getExamById(examId);
        	ResponseEntity<UserDTO> userResponse=userFeignClient.getUserProfile(id);
            if (!userResponse.getStatusCode().equals(HttpStatus.OK) || userResponse.getBody()==null) {
                throw new ResourceNotFoundException("User not found with ID: " + id);
            }
        } catch (FeignException.NotFound e) {
            throw new ResourceNotFoundException("User not found with ID: " + id);
        }
        
        
        for (AnswerSubmissionDTO ans : submissionDTO.getAnswers()) {
            // Fetch question details using simplified Feign client
            QuestionDTO question = questionBankFeignClient.QuestionById(ans.getQuestionId());
            if (question == null) {
                throw new ResourceNotFoundException("Question not found with ID: " + ans.getQuestionId());
            }

            Response response = new Response();
            response.setUserId(submissionDTO.getUserId());
            response.setExamId(examId);
            response.setQuestionId(ans.getQuestionId());
            response.setSubmittedAnswer(ans.getSubmittedAnswer());

            // Calculate marks
            int marks = 0;
            if (question.getCorrectAnswer() != null &&
                ans.getSubmittedAnswer() != null &&
                question.getCorrectAnswer().equalsIgnoreCase(ans.getSubmittedAnswer())) {
                marks = 2; // Assuming 2 marks for correct answer
            }
            response.setMarksObtained(marks);

            // Save the response
            Response savedResponse = submitResponse(response);

            // Prepare the summary
            ResponseSummaryDTO summary = new ResponseSummaryDTO(
                savedResponse.getResponseId(),
                question.getQuestionId(),
                ans.getSubmittedAnswer(),
                marks
            );
            responseSummaries.add(summary);
        }

        return ResponseEntity.ok(responseSummaries);
    }
   


}
