package com.example.demo.Repository;

import com.example.demo.Entity.Exam;
import com.example.demo.Entity.Response;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Integer> {
    // JpaRepository provides methods like save(), findById(), findAll(), deleteById(), etc.
	//List<Response> findByUserId(Integer userId);
}