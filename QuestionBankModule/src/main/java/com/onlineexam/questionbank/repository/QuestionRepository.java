package com.onlineexam.questionbank.repository;


import com.onlineexam.questionbank.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA Repository for the Question entity in the Question Bank Service.
 */
@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {
    /**
     * Finds questions by the associated exam ID.
     * @param examId The ID of the exam.
     * @return A list of questions for the given exam ID.
     */
    //List<Question> findByExamId(Integer examId);
public List<Question> findByCategory(String category);
	
	public List<Question> findByDifficulty(String difficulty);
    
}
