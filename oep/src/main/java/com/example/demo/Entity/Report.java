package com.example.demo.Entity;

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
    @ManyToOne
    @JoinColumn(name="exam_id",nullable=false)
    private Exam exam;
    @ManyToOne
    @JoinColumn(name="user_id",nullable=false)
    private User user;
    private Integer totalMarks;
    private String performanceMetrics;
    
     
    

 // Parameterized constructor for easy object creation
    /*public Report(Integer examId, Integer userId, Integer totalMarks, String performanceMetrics) {
        this.examId = examId;
        this.userId = userId;
        this.totalMarks = totalMarks;
        this.performanceMetrics = performanceMetrics;
    }
    */
    
    
}
