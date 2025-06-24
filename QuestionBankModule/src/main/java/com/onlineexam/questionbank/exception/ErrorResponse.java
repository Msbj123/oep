package com.onlineexam.questionbank.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) for structuring error responses sent back to the client.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
	private String message;
}
