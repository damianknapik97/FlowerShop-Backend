package com.dknapik.flowershop.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Extends standard Exception class by adding HTTP status to constructor
 * 
 * @author Damian
 *
 */
public class DataProcessingException extends Exception implements WebExceptionInt {
	private static final long serialVersionUID = 1L;
	private HttpStatus status;

	public DataProcessingException(String message, HttpStatus status) {
		super(message);
		this.status = status;
	}
	
	public DataProcessingException(String message) {
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
