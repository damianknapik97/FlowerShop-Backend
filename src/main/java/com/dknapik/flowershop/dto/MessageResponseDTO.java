package com.dknapik.flowershop.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Used to send simple string message that will be easily parsed
 * and provide details about processed data status.
 *
 * @author Damian
 */
@Data
@NoArgsConstructor
public class MessageResponseDTO {
    @NotNull
    private @Valid String message;

    public MessageResponseDTO(String message) {
        this.message = message;
    }
}
