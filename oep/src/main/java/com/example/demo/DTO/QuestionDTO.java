
package com.example.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDTO {
	private int questionId;
	private String text;
	private String category;
	private String difficulty;
	private String option1;
	private String option2;
	private String correctAnswer;
}
