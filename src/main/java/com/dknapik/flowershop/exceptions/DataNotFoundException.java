package com.dknapik.flowershop.exceptions;

import org.springframework.http.HttpStatus;

public class DataNotFoundException extends Exception implements WebExceptionInt {
	


	private static final long serialVersionUID = 1L;
	private HttpStatus status;

	public DataNotFoundException(String message, HttpStatus status) {
		super(message);
		this.status = status;
	}
	
	public DataNotFoundException(String message) {
		super(message);
		this.status = HttpStatus.UNPROCESSABLE_ENTITY;
	}

	@Override
	public HttpStatus getHttpStatus() {
		return this.status;
	}

	@Override
	public void setHttpStatus(HttpStatus status) {
		this.status = status;
	}
	
}
