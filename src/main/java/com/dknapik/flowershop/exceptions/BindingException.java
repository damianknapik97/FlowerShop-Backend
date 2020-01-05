package com.dknapik.flowershop.exceptions;

import lombok.ToString;
import org.springframework.http.HttpStatus;

/**
 * Used when request couldn't be mapped to provided dto class
 *
 * @author Damian
 */
@ToString
public class BindingException extends Exception implements WebExceptionInt {
    private static final long serialVersionUID = 1L;
    protected Class<?> bindingClass;    // Retrieve target class informations for debugging
    protected String userExceptionMsg;
    protected HttpStatus httpStatus;

    public BindingException(String exceptionMsg, Class<?> bindingClass) {
        super(exceptionMsg);
        this.userExceptionMsg = exceptionMsg;
        this.bindingClass = bindingClass.getClass();
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }

    public BindingException(String exceptionMsg, Class<?> bindingClass, HttpStatus httpStatus) {
        super(exceptionMsg);
        this.userExceptionMsg = exceptionMsg;
        this.bindingClass = bindingClass.getClass();
        this.httpStatus = httpStatus;
    }

    @Override
    public String getMessage() {
        return new StringBuilder("Couldn't bind data from the request to ")
                .append(bindingClass.getName())
                .append(" : ")
                .append(this.userExceptionMsg)
                .toString();
    }

    @Override
    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }

    @Override
    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}
