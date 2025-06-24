package com.onlineexam.questionbank.exception;

/**
 * Custom runtime exception class for handling specific business logic errors
 * within the Question Bank Service.
 */
public class CustomException extends RuntimeException {
    public CustomException(String message) {
        super(message);
    }
}
