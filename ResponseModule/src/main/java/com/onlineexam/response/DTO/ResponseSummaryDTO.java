package com.onlineexam.response.DTO;

import lombok.Data;
 
@Data
public class ResponseSummaryDTO {
    private Integer responseId;
    private Integer questionId;
    private String submittedAnswer;
    private Integer marksObtained;
 
    // Constructors
    public ResponseSummaryDTO(Integer responseId, Integer questionId, String submittedAnswer, Integer marksObtained) {
        this.responseId = responseId;
        this.questionId = questionId;
        this.submittedAnswer = submittedAnswer;
        this.marksObtained = marksObtained;
    }
 
    // Getters and Setters
    // (or use Lombok @Data for brevity)
}