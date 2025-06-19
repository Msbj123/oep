package com.example.demo.Repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.Entity.Response;

@Repository
public interface ResponseRepository extends JpaRepository<Response, Long> {
	

	//<Response> findByUserId(Long userId);

	//List<Response> findByUserId(Integer userId);

	//List<Response> findByUserIdAndExamExamId(Integer userId, Integer examId);

	List<Response> findByExamExamId(Integer examId);

	List<Response> findByUserId(Integer userId);

	List<Response> findByUserIdAndExamExamId(Integer userId, Integer examId);

}
