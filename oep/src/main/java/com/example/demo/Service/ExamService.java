package com.example.demo.Service;

import com.example.demo.DTO.ExamDTO;
import com.example.demo.DTO.ExamResponseDTO;
import com.example.demo.Entity.Exam;
import com.example.demo.Entity.Role;
import com.example.demo.Entity.User;
import com.example.demo.Exception.CustomException;
import com.example.demo.Repository.ExamRepository;
import com.example.demo.Repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExamService {
    @Autowired
    private ExamRepository examRepository;
    
    @Autowired
    private UserRepository userRepository;

    public ExamResponseDTO createExam(ExamDTO examDTO) {
    	//Convert DTO to Entity
        Exam exam = new Exam();
        exam.setTitle(examDTO.getTitle());
        exam.setDescription(examDTO.getDescription());
        exam.setDuration(examDTO.getDuration());
        exam.setTotalMarks(examDTO.getTotalMarks());
        //Save Entity to Database
        Exam savedExam = examRepository.save(exam);
        //Convert entity back to Response DTO
        return convertToExamResponseDTO(savedExam);
    }

    public List<ExamResponseDTO> getAllExams() {
    	//Get all entities from DB
        return examRepository.findAll().stream()
        		//Convert each entity to DTO using stream API
                .map(this::convertToExamResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<ExamResponseDTO> getExamById(Integer id) {
    	//Find Entity by Id
        return examRepository.findById(id)
        		//If found,convert to DTO
                .map(this::convertToExamResponseDTO);
    }
    /*
    public Exam getExamById(Long id)
    {
    	return examRepository.findById(id).orElse(null);
    }
    */

    public ExamResponseDTO updateExam(Integer id, ExamDTO examDetails) {
        Optional<Exam> optionalExam = examRepository.findById(id);
        if (optionalExam.isPresent()) {
            Exam existingExam = optionalExam.get();
            existingExam.setTitle(examDetails.getTitle());
            existingExam.setDescription(examDetails.getDescription());
            existingExam.setDuration(examDetails.getDuration());
            existingExam.setTotalMarks(examDetails.getTotalMarks());
            //Update fields from DTO
            Exam updatedExam = examRepository.save(existingExam);
            return convertToExamResponseDTO(updatedExam);
        } else {
            return null; 
        }
    }

    public boolean deleteExam(Integer id) {
        if (examRepository.existsById(id)) {
            examRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public boolean examExists(Integer examId) {
        return examRepository.existsById(examId);
    }
    
    public void assignRole(Integer userId, Role role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("User not found"));
        user.setRole(role);
        userRepository.save(user);
    }

    // Helper method to convert Entity to DTO
    private ExamResponseDTO convertToExamResponseDTO(Exam exam) {
        return new ExamResponseDTO(
                exam.getExamId(),
                exam.getTitle(),
                exam.getDescription(),
                exam.getDuration(),
                exam.getTotalMarks()
        );
    }
}