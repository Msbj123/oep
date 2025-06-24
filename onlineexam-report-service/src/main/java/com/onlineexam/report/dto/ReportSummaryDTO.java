package com.onlineexam.report.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportSummaryDTO {
    private Integer reportId;
    private Integer examId;
    private Integer userId;
    private Integer totalMarks;
    private String performanceMetrics;
}
