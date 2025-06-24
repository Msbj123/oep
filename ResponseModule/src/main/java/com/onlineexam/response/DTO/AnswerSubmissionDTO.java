package com.onlineexam.response.DTO;

import lombok.Data;

@Data
public class AnswerSubmissionDTO {
	private Integer questionId;
	private String submittedAnswer;

}
