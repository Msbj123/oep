package com.example.demo.DTO;
	 
	//import lombok.AllArgsConstructor;
import lombok.Data;
	//import lombok.NoArgsConstructor;
	 
	@Data
	//@NoArgsConstructor
	//@AllArgsConstructor
	public class UserResponseDTO {
	    private Integer responseId;
	    private Integer questionId;
	    private String submittedAnswer;
	    private Integer marksObtained;
	    private Integer examId;
	    private Integer userId;
	 
	    public UserResponseDTO( Integer userId,Integer questionId, String submittedAnswer, Integer marksObtained, Integer examId,Integer responseId) {
	        this.responseId = responseId;
	        this.questionId = questionId;
	        this.submittedAnswer = submittedAnswer;
	        this.marksObtained = marksObtained;
	        this.examId = examId;
	        this.userId=userId;
	        }
	   
	    // Getters and Setters
	}

