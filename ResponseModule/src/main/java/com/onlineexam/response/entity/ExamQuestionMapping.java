package com.onlineexam.response.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entity to store the mapping between Exams and Questions.
 * This table links Exam IDs (from Admin Service) to Question IDs (from Question Bank Service).
 * It's managed within the Exam Management Service.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="exam_question_mapping") // Table to store the mapping
public class ExamQuestionMapping {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id; // Unique ID for this mapping entry

    @Column(name="exam_id", nullable=false)
    private Integer examId; // ID of the Exam (from Admin Service)

    @Column(name="question_id", nullable=false)
    private Integer questionId; // ID of the Question (from Question Bank Service)

    // Optional: Add more fields for mapping details if needed (e.g., order of question in exam)
    // @Column(name="question_order")
    // private Integer questionOrder;
}

