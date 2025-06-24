package com.onlineexam.report.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

// This DTO is needed in the Report service because it's the expected return type
// from the Feign client call to the Response service.
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseSummaryDTO {
    private Integer responseId;
    private Integer questionId;
    private String submittedAnswer;
    private Integer marksObtained;
}
