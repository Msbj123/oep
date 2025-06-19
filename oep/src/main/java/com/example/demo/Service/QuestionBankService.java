package com.example.demo.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Repository.QuestionBankRepo;

import com.example.demo.Entity.QuestionBank;
import com.example.demo.Exception.EmptyFileException;


@Service
public class QuestionBankService {
	
	@Autowired
	private QuestionBankRepo qbRepo;

	public QuestionBank addQues(QuestionBank q)
	{
		return qbRepo.save(q);
	}
	
	public void addMulQues(List<QuestionBank> question) {
		for(QuestionBank qb:question)
		{
			qbRepo.save(qb);
		}
	}
	
	public void saveQuestionsFromFile(MultipartFile file) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
		String line;
		boolean skipHeader = true;
		boolean hasData = false;
		
		while ((line = reader.readLine()) != null) {
			if (skipHeader) {
				skipHeader = false;
				continue;
			}
			
			String[] data = line.split(",");
			
			QuestionBank q = new QuestionBank();
					q.setText(data[0]);
					q.setCategory(data[1]);
					q.setDifficulty(data[2]);
					q.setOption1(data[3]);
					q.setOption2(data[4]);
					q.setCorrectAnswer(data[5]);
					addQues(q);
					hasData=true;
			}
		if(!hasData)
		{
			throw new EmptyFileException("The uploaded file contains only headers and no data rows.");
		}
	}

	public List<QuestionBank> getAll()
	{
		return qbRepo.findAll();
	}
	
	public Optional<QuestionBank> getById(int id)
	{
		return qbRepo.findById(id);
	}
	
	public List<QuestionBank> getQuestionByCategory(String category)
	{
		return qbRepo.findByCategory(category);
	}
	
	public List<QuestionBank> getQuestionByDifficulty(String difficulty)
	{
		return qbRepo.findByDifficulty(difficulty);
	}
	
	public Optional<QuestionBank> update(int id,QuestionBank qb)
	{
		return qbRepo.findById(id).map(question->{
			question.setText(qb.getText());
			question.setCategory(qb.getCategory());
			question.setDifficulty(qb.getDifficulty());
			question.setOption1(qb.getOption1());
			question.setOption2(qb.getOption2());
			question.setCorrectAnswer(qb.getCorrectAnswer());
			
			return qbRepo.save(question);
		});
	}
	
	public void delete(int id)
	{
		if(qbRepo.existsById(id))
		{
			qbRepo.deleteById(id);	
		}
		else {
			throw new NoSuchElementException("Question with ID "+ id+" not found");
		}
	}

	public List<QuestionBank> getQuestions() {
		// TODO Auto-generated method stub
		return qbRepo.findAll();
	}	
}
