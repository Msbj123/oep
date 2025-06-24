package com.onlineexam.admin.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamDTO {
    @NotBlank(message = "Title cannot be blank")
    private String title;

    private String description;

    @NotNull(message = "Duration cannot be null")
    @Min(value = 1, message = "Duration must be at least 1 minute")
    private Integer duration; // Duration in minutes

    @NotNull(message = "Total marks cannot be null")
    @Min(value = 1, message = "Total marks must be at least 1")
    private Integer totalMarks;
}