package com.example.demo.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DTO.QuestionAttemptDTO;
import com.example.demo.DTO.QuestionDTO;
import com.example.demo.Entity.Exam;
import com.example.demo.Entity.ExamQuestionMapping;
import com.example.demo.Entity.QuestionBank;
import com.example.demo.Repository.ExamRepository;
import com.example.demo.Repository.QuestionBankRepo;
import com.example.demo.Service.ExamQuestionMappingService;

@RestController
@RequestMapping("/mapping")
public class ExamQuestionMappingController {
	
	@Autowired
	private ExamQuestionMappingService mappingService;
	@Autowired
	private ExamRepository examRepository;
	@Autowired
	private QuestionBankRepo questionRepo;
	@Autowired
	private ExamQuestionMappingService eqms;
	
	@PostMapping("/{examId}/{questionId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ExamQuestionMapping mapQuestionToExam(@PathVariable int examId, @PathVariable int questionId) {
		Exam exam=examRepository.findById(examId).orElseThrow();
		
		QuestionBank question=questionRepo.findById(questionId).orElseThrow();
		
		return mappingService.saveMapping(exam,question);
	}
	
	@GetMapping("/exam/{examId}")
	@PreAuthorize("hasRole('ADMIN')")
	public List<ExamQuestionMapping> getQuestionsByExam(@PathVariable int examId){
		Exam exam=examRepository.findById(examId).orElseThrow();
		return mappingService.getMappingsByExam(exam);
	}
	
	@GetMapping("/allexams")
	@PreAuthorize("hasRole('ADMIN')")
	public List<ExamQuestionMapping> getAllExams()
	{
		return mappingService.getAllExams();
	}
	
	@GetMapping("/exam/{examId}/questions")
	@PreAuthorize("hasRole('STUDENT')")
	public ResponseEntity<List<QuestionAttemptDTO>> getExamQuestions(@PathVariable int examId)
	{
		List<QuestionAttemptDTO> questions = mappingService.getAllQuestions(examId);
		return ResponseEntity.ok(questions);	
	}
	@GetMapping("/exam/{examId}/adminQues")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<QuestionDTO>> getExamQuestionsAdmin(@PathVariable int examId)
	{
		List<QuestionDTO> questions = mappingService.getAllQuestionsAdmin(examId);
		return ResponseEntity.ok(questions);	
	}
	
}
