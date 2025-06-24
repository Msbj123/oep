package com.onlineexam.admin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) for creating or updating a Question in the Question Bank.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDTO {

    @NotBlank(message = "Question text cannot be blank")
    private String questionText;

    private String category;
    private String difficulty;

    @NotBlank(message = "Correct answer cannot be blank")
    private String correctAnswer;
}
