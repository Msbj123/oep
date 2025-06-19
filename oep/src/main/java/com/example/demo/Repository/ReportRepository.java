package com.example.demo.Repository;

import com.example.demo.Entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<Report, Integer> {
	

	/**
     * Finds all reports associated with a specific exam ID.
     *
     * @param examId the ID of the exam
     * @return a list of reports for the given exam
     */
   // List<Report> findByExamId(Integer examId);

    /**
     * Finds all reports associated with a specific user ID.
     *
     * @param userId the ID of the user
     * @return a list of reports for the given user
     */
    //List<Report> findByUserId(Integer userId);

    /**
     * Finds a report for a specific user and exam combination.
     *
     * @param userId the ID of the user
     * @param examId the ID of the exam
     * @return an optional containing the report if found, or empty otherwise
     */
    //Optional<Report> findByUserIdAndExamId(Integer userId, Integer examId);
    

    /**
     * Retrieves the user ID and total marks for each user, ordered by total marks in descending order.
     * Used to determine the top-performing user(s).
     *
     * @return a list of object arrays where each array contains [userId, totalMarks]
     */
	@Query("SELECT r FROM Report r WHERE r.totalMarks = (SELECT MAX(r2.totalMarks) FROM Report r2)")
	List<Report> findTopperByTotalMarks();



	List<Report> findByExamExamId(Integer examId);


	List<Report> findByUserUserId(Integer userId);


	//Optional<Report> findByUserUserIdAndExamExamId(Integer userId, Integer examId);


	Optional<Report> findByUserUserIdAndExamExamId(Integer userId, Integer examId);


}
