package com.example.demo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.example.demo.Entity.QuestionBank;

@Repository
public interface QuestionBankRepo extends JpaRepository<QuestionBank,Integer>{
	public List<QuestionBank> findByCategory(String category);
	
	public List<QuestionBank> findByDifficulty(String difficulty);
}
