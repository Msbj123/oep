package com.onlineexam.response.DTO;

import java.util.List;

import lombok.Data;

@Data
public class ExamSubmissionDTO {
	private Integer userId;
	//private Integer examId;
	private List<AnswerSubmissionDTO> answers;
}
