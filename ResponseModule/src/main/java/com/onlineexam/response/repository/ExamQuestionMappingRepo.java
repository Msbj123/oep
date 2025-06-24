package com.onlineexam.response.repository;

import com.onlineexam.response.entity.ExamQuestionMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA Repository for ExamQuestionMapping entity in the Exam Management Service.
 */
@Repository
public interface ExamQuestionMappingRepo extends JpaRepository<ExamQuestionMapping , Long> {
    /**
     * Finds all mappings for a specific exam ID.
     * @param examId The ID of the exam.
     * @return A list of ExamQuestionMapping entities.
     */
    List<ExamQuestionMapping> findByExamId(Integer examId);

    /**
     * Finds all mappings for a specific question ID.
     * @param questionId The ID of the question.
     * @return A list of ExamQuestionMapping entities.
     */
    List<ExamQuestionMapping> findByQuestionId(Integer questionId);

    /**
     * Checks if a specific mapping between an exam and a question already exists.
     * @param examId The ID of the exam.
     * @param questionId The ID of the question.
     * @return true if the mapping exists, false otherwise.
     */
    boolean existsByExamIdAndQuestionId(Integer examId, Integer questionId);
}

