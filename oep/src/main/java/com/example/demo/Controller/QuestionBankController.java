package com.example.demo.Controller;

import com.example.demo.DTO.*;
import com.example.demo.Mapper.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Entity.QuestionBank;
import com.example.demo.Exception.EmptyFileException;
import com.example.demo.Service.QuestionBankService;


@RestController
@RequestMapping("/qb")
public class QuestionBankController {
	
	@Autowired
	private QuestionBankService qbService;
	
	/**
	 * Adding a single question to the database
	 * @param dto
	 * @return ResponseEntity
	 */
	@PostMapping("/addQuestion")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<QuestionDTO> addQuestion(@RequestBody QuestionDTO dto) {
		QuestionBank saved=QuestionMapper.toEntity(dto);
		qbService.addQues(saved);
		return ResponseEntity.ok(dto);
	}
	/**
	 * Adding multiple questions to the database
	 * @param dtos
	 * @return ResponseEntity<List<QuestionDTO>>
	 */
	@PostMapping("/addMultipleQuestions")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<QuestionDTO>> addMultipleQuestions(@RequestBody List<QuestionDTO> dtos) {
		List<QuestionBank> questions = dtos.stream()
			.map(QuestionMapper::toEntity)   //It is method reference
											 // map(dto->QuestionMapper.toEntity(dto)) 
			.toList();
		qbService.addMulQues(questions);
		return ResponseEntity.ok(questions.stream().map(QuestionMapper::toDTO).toList());
	}
	/**
	 * Adding questions from the file
	 * @param file
	 * @return ResponseEntity<String>
	 */
@PostMapping("/uploadFile")
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
	if (file.isEmpty()) {
		throw new EmptyFileException("Uploaded file is empty. Please select a valid file.");
		}
	try {
		qbService.saveQuestionsFromFile(file);	
		return ResponseEntity.ok("File uploaded and data saved successfully.");
	} catch (Exception e) {
		return ResponseEntity.badRequest().body("Error processing file: " + e.getMessage());
	}
}

	
/**
 * Retrieving the question with the id
 * @param id
 * @return ResponseEntity<QuestionDTO>
 */
@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/getquestion/{id}")
	public ResponseEntity<QuestionDTO> getQuestionById(@PathVariable int id) {
		return qbService.getById(id)
			.map(QuestionMapper::toDTO) //object->QuestionMapper.toDTO(object)
			.map(ResponseEntity::ok)    //dto->ResponseEntity.ok(dto)
			.orElse(ResponseEntity.notFound().build());
}
	
	//
	/**
	 * Retrieving all the questions
	 * @return List<QuestionDTO>
	 */
	@GetMapping("/getAll")
	@PreAuthorize("hasAnyRole('ADMIN', 'STUDENT')")
	public List<QuestionDTO> getAllQuestions() {
		return qbService.getAll().stream()
		.map(QuestionMapper::toDTO)
		.toList();
}
	@GetMapping("/getAllQuestions")
	@PreAuthorize("hasRole('ADMIN')")
	public List<QuestionBank> getQuestions()
	{
		return qbService.getQuestions();
	}
	
	 
	/**
	 * For student to attempt all questions
	 * @return List<QuestionAttemptDTO>
	 */
	@GetMapping("/attemptQuestions")
	@PreAuthorize("hasAnyRole('ADMIN', 'STUDENT')")
	public List<QuestionAttemptDTO> getQuestionsForAttempt() {
		return qbService.getAll().stream()
			.map(QuestionMapper::toAttemptDTO)
			.toList();
}

	/**
	 * For updating a question
	 * @param id
	 * @param dto
	 * @return ResponseEntity<QuestionDTO>
	 */
	
	@PutMapping("/updQuestion/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<QuestionDTO> updateQuestion(@PathVariable int id, @RequestBody QuestionDTO dto) {
		return qbService.update(id, QuestionMapper.toEntity(dto))
		.map(QuestionMapper::toDTO) 
		.map(ResponseEntity::ok)
		.orElse(ResponseEntity.notFound().build());
}

	
	/**
	 * Deleting a question with id
	 * @param id
	 * @return ResponseEntity<String>
	 */
	@DeleteMapping("/delQuestion/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> deleteQuestion(@PathVariable int id)
	{
		qbService.delete(id);
		return ResponseEntity.ok("Deleted the question with id "+id);
	}
	/**
	 * Retrieving questions based on category
	 * @param category
	 * @return List<QuestionDTO>
	 */
	@GetMapping("/getByCategory/{category}")
	@PreAuthorize("hasRole('ADMIN')")
	public List<QuestionDTO> getByCategory(@PathVariable String category) {
		return qbService.getQuestionByCategory(category).stream()
				.map(QuestionMapper::toDTO)
				.toList();
}
	/**
	 * //Retrieving Questions based on difficulty
	 * @param difficulty
	 * @return List<QuestionDTO>
	 */
	@GetMapping("/getByDifficulty/{difficulty}")
	@PreAuthorize("hasRole('ADMIN')")
	public List<QuestionDTO> getByDifficulty(@PathVariable String difficulty) {
		return qbService.getQuestionByDifficulty(difficulty).stream()
		.map(QuestionMapper::toDTO)
		.toList();
}


}
