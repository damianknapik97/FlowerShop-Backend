package com.dknapik.flowershop.dto;

/**
 * Used to send simple string message that will be easily parsed
 * and provide details about processed data status.
 * 
 * @author Damian
 *
 */
public class MessageResponseDto {
	private String message;

	public String getMessage() {
		return message;
	}

	public MessageResponseDto(String message) {
		this.message = message;
	}
	
	public MessageResponseDto() { }

	public void setMessage(String message) {
		this.message = message;
	}
	
	

}
