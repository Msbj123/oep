package com.example.demo.Controller;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DTO.AnswerSubmissionDTO;
//import com.example.demo.DTO.ExamResponseDTO;
import com.example.demo.DTO.ExamSubmissionDTO;
import com.example.demo.DTO.ResponseSummaryDTO;
import com.example.demo.DTO.UserResponseDTO;
import com.example.demo.Entity.Exam;
import com.example.demo.Entity.QuestionBank;
import com.example.demo.Entity.Response;
import com.example.demo.Exception.ResourceNotFoundException;
import com.example.demo.Repository.ExamRepository;
import com.example.demo.Service.ExamService;
import com.example.demo.Service.QuestionBankService;
import com.example.demo.Service.ResponseService;

@RestController
@RequestMapping("/api/exams")
public class ExamManagementController {

    //@Autowired
    //private ExamService examService;

    @Autowired
    private QuestionBankService questionService;

    @Autowired
    private ResponseService responseService;
    
    @Autowired
    private ExamRepository examRepository;

    @PostMapping("/{examId}/submit")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<ResponseSummaryDTO>> submitExam(@PathVariable Integer examId, @RequestBody ExamSubmissionDTO submissionDTO) {
        /*List<ResponseSummaryDTO> responseSummaries = new ArrayList<>();
        Optional<Exam> examOpt = examRepository.findById(examId);

        if (!examOpt.isPresent()) {
            throw new ResourceNotFoundException("Exam not found with ID: " + examId);
        }

        Exam exam = examOpt.get();

        for (AnswerSubmissionDTO ans : submissionDTO.getAnswers()) {
            Optional<QuestionBank> questionOpt = questionService.getById(ans.getQuestionId());

            if (!questionOpt.isPresent()) {
                throw new ResourceNotFoundException("Question not found with ID: " + ans.getQuestionId());
            }

            QuestionBank question = questionOpt.get();
           
            
            Response response = new Response();
            response.setUserId(submissionDTO.getUserId());
            response.setExam(exam);
            response.setQuestion(question);
            response.setSubmittedAnswer(ans.getSubmittedAnswer());

           /* int marks = question.getCorrectAnswer().equals(ans.getSubmittedAnswer()) ? question.getMaxMarks() : 0;
            response.setMarksObtained(marks);
			int marks=0;
            Response savedResponse = responseService.submitResponse(response);

            ResponseSummaryDTO summary = new ResponseSummaryDTO(
                    savedResponse.getResponseId(),
                    question.getQuestionId(),
                    ans.getSubmittedAnswer(),
                    marks
            );
            responseSummaries.add(summary);
        }

        return ResponseEntity.ok(responseSummaries);
        */
    	return responseService.submitExam(examId,submissionDTO);
    }

    @GetMapping("/responses/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN','STUDENT')")
    public ResponseEntity<Map<Integer, Map<Integer, List<UserResponseDTO>>>> getUserResponse(@PathVariable Integer userId) {
        List<Response> responses = responseService.getResponsesByUser(userId);

        List<UserResponseDTO> userResponses = responses.stream()
                .map(r -> new UserResponseDTO(
                        r.getUserId(),
                        r.getQuestion().getQuestionId(),
                        r.getSubmittedAnswer(),
                        r.getMarksObtained(),
                        r.getExam().getExamId(),
                        r.getResponseId()
                ))
                .collect(Collectors.toList());

        Map<Integer, Map<Integer, List<UserResponseDTO>>> grouped = userResponses.stream()
                .collect(Collectors.groupingBy(
                        UserResponseDTO::getUserId,
                        Collectors.groupingBy(UserResponseDTO::getExamId)
                ));

        return ResponseEntity.ok(grouped);
    }

    @GetMapping("/exam/{examId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Response>> getResponsesByExamId(@PathVariable Integer examId) {
        List<Response> responses = responseService.getResponsesByExam(examId);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @GetMapping("/{examId}/responses/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponseDTO>> getUserResponsesForExam(@PathVariable Integer examId, @PathVariable Integer userId) {
        List<Response> responses = responseService.getResponsesByUserAndExam(userId, examId);

        List<UserResponseDTO> userResponseDTOs = responses.stream()
                .map(r -> new UserResponseDTO(
                        r.getUserId(),
                        r.getQuestion().getQuestionId(),
                        r.getSubmittedAnswer(),
                        r.getMarksObtained(),
                        r.getExam().getExamId(),
                        r.getResponseId()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(userResponseDTOs);
    }
}
