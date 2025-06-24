package com.onlineexam.response.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamResponseDTO {
    private Integer examId;
    private String title;
    private String description;
    private Integer duration;
    private Integer totalMarks;
}