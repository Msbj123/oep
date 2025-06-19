package com.example.demo.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Mapper.QuestionMapper;
import com.example.demo.DTO.QuestionAttemptDTO;
import com.example.demo.DTO.QuestionDTO;
import com.example.demo.Entity.Exam;
import com.example.demo.Entity.ExamQuestionMapping;
import com.example.demo.Entity.QuestionBank;
import com.example.demo.Repository.ExamQuestionMappingRepo;
import com.example.demo.Repository.ExamRepository;
import com.example.demo.Repository.QuestionBankRepo;

@Service
public class ExamQuestionMappingService {

	@Autowired
	private ExamQuestionMappingRepo mappingRepository;
	
	@Autowired
	private QuestionBankRepo qbRepo;
	@Autowired
	private ExamRepository examRepo;
	public ExamQuestionMapping saveMapping(Exam exam,QuestionBank question)
	{
		ExamQuestionMapping mapping=new ExamQuestionMapping();
		mapping.setExam(exam);
		mapping.setQuestion(question);
		return mappingRepository.save(mapping);
	}
	
	public List<ExamQuestionMapping> getMappingsByExam(Exam exam){
		return mappingRepository.findByExam(exam);
	}
	
	public List<ExamQuestionMapping> getMappingsByQuestion(QuestionBank question)
	{
		return mappingRepository.findByQuestion(question);
	}
	
	public List<ExamQuestionMapping> getAllExams()
	{
		return mappingRepository.findAll();
	}
	/*
	public List<QuestionAttemptDTO> getAllQuestionsForExam(int examId)
	{
		Exam exam=examRepo.findById(examId).orElseThrow();
		List<ExamQuestionMapping> mappings=qbRepo.findAll().stream()
				.filter(q->q.)
				.collect(Collectors.toList());
	}
	*/
	
	public  List<QuestionAttemptDTO> getAllQuestions(int examId)
	{
		List<ExamQuestionMapping> mappings=mappingRepository.findByExamExamId(examId);
		return mappings.stream()
				.map(QuestionMapper::mapToQuestionAttemptDTO)
				.collect(Collectors.toList());
	}

	public List<QuestionDTO> getAllQuestionsAdmin(int examId) {
		// TODO Auto-generated method stub
		List<ExamQuestionMapping> mappings=mappingRepository.findByExamExamId(examId);
		return mappings.stream()
				.map(QuestionMapper::mapToQuestionDTO)
				.collect(Collectors.toList());
	}
	

}