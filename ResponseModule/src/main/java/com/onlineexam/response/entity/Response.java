package com.onlineexam.response.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Represents a user's response to a question in an exam.
 * This entity belongs to the Exam Management Service and is stored in its dedicated database.
 * It links to Exam, Question, and User entities via their IDs, which are owned
 * by other microservices.
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="ExamResponse") // Renamed table to avoid potential conflict if 'Response' is a reserved keyword
public class Response {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer responseId;

	@Column(name="user_id", nullable=false)
	private Integer userId; // Foreign Key to User Service's User entity ID

	@Column(name="exam_id", nullable=false)
	private Integer examId; // Foreign Key to Admin Service's Exam entity ID

	@Column(name="question_id", nullable=false)
	private Integer questionId; // Foreign Key to Question Bank Service's Question entity ID

	@Column(name="submitted_answer", columnDefinition = "TEXT")
	private String submittedAnswer;

	@Column(name="marks_obtained")
	private Integer marksObtained;

}
