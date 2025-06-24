package com.onlineexam.response.repository;

import com.onlineexam.response.entity.Response;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA Repository for the Response entity in the Exam Management Service.
 */
@Repository
public interface ResponseRepository extends JpaRepository<Response, Integer> {
    /**
     * Finds all responses by a specific user for a given exam.
     * @param userId The ID of the user.
     * @param examId The ID of the exam.
     * @return A list of Response entities.
     */
    List<Response> findByUserIdAndExamId(Integer userId, Integer examId);

    /**
     * Finds all responses for a given exam, across all users.
     * @param examId The ID of the exam.
     * @return A list of Response entities.
     */
    List<Response> findByExamId(Integer examId);

    /**
     * Finds all responses submitted by a specific user, across all exams.
     * @param userId The ID of the user.
     * @return A list of Response entities.
     */
    List<Response> findByUserId(Integer userId);
}

