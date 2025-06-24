package com.onlineexam.questionbank.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.onlineexam.questionbank.dto.QuestionDTO;
import com.onlineexam.questionbank.entity.Question;
import com.onlineexam.questionbank.exception.EmptyFileException;
import com.onlineexam.questionbank.mapper.QuestionMapper;
import com.onlineexam.questionbank.repository.QuestionRepository;




@Service
public class QuestionService {
	
	@Autowired
	private QuestionRepository qbRepo;

	public Question addQues(Question q)
	{
		return qbRepo.save(q);
	}
	
	public void addMulQues(List<Question> question) {
		for(Question qb:question)
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
			
			Question q = new Question();
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

	public List<Question> getAll()
	{
		return qbRepo.findAll();
	}
	
	public Optional<Question> getById(int id)
	{
		return qbRepo.findById(id);
	}
	
	public List<Question> getQuestionByCategory(String category)
	{
		return qbRepo.findByCategory(category);
	}
	
	public List<Question> getQuestionByDifficulty(String difficulty)
	{
		return qbRepo.findByDifficulty(difficulty);
	}
	
	public Optional<Question> update(int id,Question qb)
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

	public List<Question> getQuestions() {
		// TODO Auto-generated method stub
		return qbRepo.findAll();
	}

	public ResponseEntity<QuestionDTO> getQuestionById(Integer id) {
	    return qbRepo.findById(id)
	        .map(question -> ResponseEntity.ok(QuestionMapper.toDTO(question)))
	        .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}
	public Question getQuesById(Integer id) {
        return qbRepo.findById(id).orElse(null);
              
}

	public QuestionDTO getQuestionDTOById(Integer id) {
		// TODO Auto-generated method stub
		return qbRepo.findById(id).map(QuestionMapper::toDTO).orElse(null);
	}
}