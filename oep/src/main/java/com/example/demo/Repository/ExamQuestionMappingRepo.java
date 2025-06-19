package com.example.demo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Entity.Exam;
import com.example.demo.Entity.ExamQuestionMapping;
import com.example.demo.Entity.QuestionBank;

public interface ExamQuestionMappingRepo extends JpaRepository<ExamQuestionMapping , Long>{

	List<ExamQuestionMapping> findByExam(Exam exam);
	
	List<ExamQuestionMapping> findByQuestion(QuestionBank question);
	
	List<ExamQuestionMapping> findByExamExamId(int examId);
}
