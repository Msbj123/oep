package com.onlineexam.report.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@NoArgsConstructor   //Default constructor
@Table(name = "reports")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reportId;

    private Integer examId; // Referencing a minimal Exam entity in this service

   
    private Integer userId; // Referencing a minimal User entity in this service

    private Integer totalMarks;
    private String performanceMetrics;
}
