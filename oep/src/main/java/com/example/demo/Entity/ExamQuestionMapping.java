package com.example.demo.Entity;

import jakarta.persistence.*;
import lombok.*;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="exam_question")
public class ExamQuestionMapping {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="exam_id")
	private Exam exam;
	
	@ManyToOne
	@JoinColumn(name="question_id")
	private QuestionBank question;
}
