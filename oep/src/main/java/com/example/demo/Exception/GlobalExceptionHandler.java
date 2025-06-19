package com.example.demo.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.example.demo.Exception.CustomException;
import com.example.demo.Exception.ErrorResponse;
import com.example.demo.Exception.ResourceNotFoundException;

import jakarta.validation.ConstraintViolationException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

	/*
    // Handle validation errors like @Pattern, @NotBlank, etc.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage())
        );
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
    
    //Handle all other exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    */
   
    @ExceptionHandler(EmptyFileException.class)
    public ResponseEntity<Map<String, String>> handleEmptyFileException(EmptyFileException ex) {
    	Map<String, String> error = new HashMap<>();
    	error.put("error", ex.getMessage());
    	return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
}
    @ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ErrorResponse> RuntimeException(RuntimeException ex){
	    ErrorResponse error = new ErrorResponse(ex.getMessage());
	    return new ResponseEntity<>(error,HttpStatus.NOT_FOUND);
	}
	//Handles CustomException
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex){
    	ErrorResponse error = new ErrorResponse(ex.getMessage());
    	return new ResponseEntity<>(error,HttpStatus.NOT_FOUND);
    }

    //Handles MethodArgumentNotValidException
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult()
                           .getFieldErrors()
                           .stream()
                           .map(error -> error.getDefaultMessage())
                           .collect(Collectors.joining(", "));

        ErrorResponse errorResponse = new ErrorResponse(message);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    //Handles ConstraintViolationException
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolations(ConstraintViolationException ex) {
        String message = ex.getConstraintViolations()
                           .stream()
                           .map(v -> v.getMessage())
                           .collect(Collectors.joining(", "));

        ErrorResponse errorResponse = new ErrorResponse(message);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {
        
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("error", "Not Found");
        body.put("message", ex.getMessage());
        body.put("path", request.getDescription(false).replace("uri=", "")); // Clean up path

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }
    

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<Object> handleInvalidInput(InvalidInputException ex) {
    	Map<String, Object> body = new HashMap<>();
    	body.put("timestamp", LocalDateTime.now());
    	body.put("status", HttpStatus.BAD_REQUEST.value());
    	body.put("error", "Invalid Input");
    	body.put("message", ex.getMessage());
    	return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    	}




}





