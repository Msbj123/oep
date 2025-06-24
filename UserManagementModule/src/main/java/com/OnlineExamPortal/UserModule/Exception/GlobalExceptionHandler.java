package com.OnlineExamPortal.UserModule.Exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {


	//Handles RuntimeException
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

}

