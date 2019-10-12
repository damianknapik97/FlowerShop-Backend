package com.dknapik.flowershop.dto;

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
