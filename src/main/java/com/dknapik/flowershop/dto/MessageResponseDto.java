package com.dknapik.flowershop.dto;

/**
 * Used to send simple string message that will be easily parsed
 * and provide details about processed data status.
 *
 * @author Damian
 */
public class MessageResponseDto {
    private String message;

    public String getMessage() {
        return message;
    }

    public MessageResponseDto(String message) {
        this.message = message;
    }

    public MessageResponseDto() {
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "MessageResponseDto{" +
                "message='" + message + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MessageResponseDto that = (MessageResponseDto) o;

        return message != null ? message.equals(that.message) : that.message == null;
    }

    @Override
    public int hashCode() {
        return message != null ? message.hashCode() : 0;
    }
}
