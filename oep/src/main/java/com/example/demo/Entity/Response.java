package com.example.demo.Entity;

import java.util.Optional;

import com.example.demo.DTO.ExamResponseDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Response {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer responseId;
	private Integer userId;
	
	@ManyToOne
	@JoinColumn(name="exam_id")
	private Exam exam;
	
	//@JsonIgnore
	@ManyToOne
	@JoinColumn(name="question_id")
	private QuestionBank question;
	
	private String submittedAnswer;
	private Integer marksObtained;
}
