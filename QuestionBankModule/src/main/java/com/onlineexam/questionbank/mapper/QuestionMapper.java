package com.onlineexam.questionbank.mapper;

import com.onlineexam.questionbank.dto.QuestionDTO;
import com.onlineexam.questionbank.dto.QuestionResponseDTO;
import com.onlineexam.questionbank.entity.Question;

public class QuestionMapper {

		public static QuestionDTO toDTO(Question qb) {
			return new QuestionDTO(
				qb.getQuestionId(),
				qb.getText(),
				qb.getCategory(),
				qb.getDifficulty(),
				qb.getOption1(),
				qb.getOption2(),
				qb.getCorrectAnswer()
				);
		}

		public static QuestionResponseDTO toAttemptDTO(Question qb) {
			return new QuestionResponseDTO(
				qb.getQuestionId(),
				qb.getText(),
				qb.getCategory(),
				qb.getDifficulty(),
				qb.getOption1(),
				qb.getOption2()
				);
		}

		public static Question toEntity(QuestionDTO dto) {
			Question qb = new Question();
			qb.setQuestionId(dto.getQuestionId());
			qb.setText(dto.getText());
			qb.setCategory(dto.getCategory());
			qb.setDifficulty(dto.getDifficulty());
			qb.setOption1(dto.getOption1());
			qb.setOption2(dto.getOption2());
			qb.setCorrectAnswer(dto.getCorrectAnswer());
			return qb;
		}
		
		/*public static QuestionAttemptDTO mapToDTO(ExamQuestionMapping mapping)
		{
			QuestionBank q=mapping.getQuestion();
			return new QuestionAttemptDTO(
					q.getQuestionId(),
					q.getText(),
					q.getCategory(),
					q.getDifficulty(),
					q.getOption1(),
					q.getOption2()
					);
					
		}
		*/
		
}
