
package com.example.demo.Mapper;

import com.example.demo.DTO.QuestionDTO;
import com.example.demo.DTO.QuestionAttemptDTO;
import com.example.demo.Entity.ExamQuestionMapping;
import com.example.demo.Entity.QuestionBank;

public class QuestionMapper {

		public static QuestionDTO toDTO(QuestionBank qb) {
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

		public static QuestionAttemptDTO toAttemptDTO(QuestionBank qb) {
			return new QuestionAttemptDTO(
				qb.getQuestionId(),
				qb.getText(),
				qb.getCategory(),
				qb.getDifficulty(),
				qb.getOption1(),
				qb.getOption2()
				);
		}

		public static QuestionBank toEntity(QuestionDTO dto) {
			QuestionBank qb = new QuestionBank();
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
		public static QuestionAttemptDTO mapToQuestionAttemptDTO(ExamQuestionMapping mapping)
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
		public static QuestionDTO mapToQuestionDTO(ExamQuestionMapping mapping)
		{
			QuestionBank q=mapping.getQuestion();
			return new QuestionDTO(
					q.getQuestionId(),
					q.getText(),
					q.getCategory(),
					q.getDifficulty(),
					q.getOption1(),
					q.getOption2(),
					q.getCorrectAnswer()
					
					);
		}
		
}
