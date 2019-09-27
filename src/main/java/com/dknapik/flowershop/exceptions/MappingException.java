package com.dknapik.flowershop.exceptions;

import org.springframework.http.HttpStatus;

public class MappingException extends BindingException {

	private static final long serialVersionUID = 1L;
	protected Class<?> bindingToClass;

	public MappingException(String exceptionMsg, Class<?> bindingClass, Class<?> bindingFromClass) {
		super(exceptionMsg, bindingClass);
		this.bindingToClass = bindingFromClass;
		this.httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
	}

	public MappingException(String exceptionMsg, Class<?> bindingClass, HttpStatus httpStatus,
			Class<?> bindingFromClass) {
		super(exceptionMsg, bindingClass, httpStatus);
		this.bindingToClass = bindingFromClass;
	}


	@Override
	public String getMessage() {
		return new StringBuilder("Couldn't map data from ")
				.append(this.bindingClass.getName())
				.append(" to ")
				.append(this.bindingToClass.getName())
				.append(" : ")
				.append(this.userExceptionMsg)
				.toString();
	}
	
	
	
	
}
