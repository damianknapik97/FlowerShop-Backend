package com.dknapik.flowershop.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Enforces provision of HTTP status to exceptions
 * for easier response handling.
 * 
 * @author Damian
 *
 */
public interface WebExceptionInt {
	
	public HttpStatus getHttpStatus();
	public void setHttpStatus(HttpStatus status);
	
}
