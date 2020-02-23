package com.dknapik.flowershop.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Used to send simple string message that will be easily parsed
 * and provide details about processed data status.
 *
 * TODO: There is no point in no args constructor for this entity.
 * Unfortunately it conflicts with some of currently existing classes - refactor it in the future.
 *
 * @author Damian
 */
@Data
@NoArgsConstructor
public class MessageResponseDTO {
    @NotNull
    private @Valid String message;

    public MessageResponseDTO(@NonNull String message) {
        this.message = message;
    }

    public MessageResponseDTO(@NonNull Enum<?> message) {
        this.message = message.toString();
    }
}
