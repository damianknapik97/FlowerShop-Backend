package com.dknapik.flowershop.exceptions;

import org.springframework.http.HttpStatus;

public interface WebExceptionInt {
	
	public HttpStatus getHttpStatus();
	public void setHttpStatus(HttpStatus status);
	
}
