package com.onlineexam.admin.repository;

import com.onlineexam.admin.entity.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Integer> {
    // JpaRepository provides methods like save(), findById(), findAll(), deleteById(), etc.
}