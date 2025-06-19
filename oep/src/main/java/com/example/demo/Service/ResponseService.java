package com.example.demo.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.DTO.AnswerSubmissionDTO;
import com.example.demo.DTO.ExamSubmissionDTO;
import com.example.demo.DTO.ResponseSummaryDTO;
import com.example.demo.Entity.Exam;
import com.example.demo.Entity.QuestionBank;
import com.example.demo.Entity.Response;
import com.example.demo.Exception.ResourceNotFoundException;
import com.example.demo.Repository.ExamRepository;
import com.example.demo.Repository.QuestionBankRepo;
import com.example.demo.Repository.ResponseRepository;
import com.example.demo.Repository.UserRepository;

@Service
public class ResponseService {

    @Autowired
    private ExamRepository examRepository;
    
    @Autowired
    private QuestionBankRepo qbRepo;
    @Autowired
    private ResponseRepository responseRepository;
    @Autowired
    private UserRepository userRepo;

    public Response submitResponse(Response response) {
    	if(userRepo.existsById(response.getUserId()))
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

    public List<Response> getResponsesByExam(Integer examId) {
        if (!examRepository.existsById(examId)) {
            throw new ResourceNotFoundException("Exam not found with ID: " + examId);
        }
        return responseRepository.findByExamExamId(examId);
    }

    public List<Response> getResponsesByUserAndExam(Integer userId, Integer examId) {
        if (!examRepository.existsById(examId)) {
            throw new ResourceNotFoundException("Exam not found with ID: " + examId);
        }
        return responseRepository.findByUserIdAndExamExamId(userId, examId);
    }

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
}
