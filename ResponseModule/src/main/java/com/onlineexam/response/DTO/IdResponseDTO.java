package com.onlineexam.response.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Generic Data Transfer Object (DTO) for returning a newly created resource's ID
 * along with a confirmation message. Useful for POST requests.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IdResponseDTO {
    private Integer id;
    private String message;
}

