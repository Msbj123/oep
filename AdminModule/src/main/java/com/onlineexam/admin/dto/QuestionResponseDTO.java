package com.onlineexam.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) for sending Question details as a response from the Question Bank.
 * Note: It includes examId but not examTitle, as the title resides in the Admin Service.
 * If examTitle is needed, it would require another cross-service call or data duplication strategy.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionResponseDTO {
    private Integer questionId;
    private String questionText;
    private String category;
    private String difficulty;
    private String correctAnswer;
    private Integer examId; // The ID of the exam this question is associated with
    // private String examTitle; // Removed: Admin service needs to get this if required for display
}
