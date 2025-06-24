package com.onlineexam.response.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception to indicate that a requested resource was not found.
 * This will typically result in an HTTP 404 Not Found response.
 */
@ResponseStatus(HttpStatus.NOT_FOUND) // Makes Spring respond with 404
public class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L; // Recommended for Serializable classes

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
