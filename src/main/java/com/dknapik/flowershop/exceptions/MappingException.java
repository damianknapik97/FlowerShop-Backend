package com.dknapik.flowershop.exceptions;

import lombok.ToString;
import org.springframework.http.HttpStatus;

/**
 * Used when unexpected data prevented correct mapping from one class to another class.
 *
 * @author Damian
 */
@ToString(callSuper = true)
public class MappingException extends BindingException {
    private static final long serialVersionUID = 1L;
    protected Class<?> bindingFromClass; // Retrieve source class informations for debugging


    public MappingException(Enum e, Class<?> bindingClass, Class<?> bindingFromClass) {
        super(e.toString(), bindingClass);
        this.bindingFromClass = bindingFromClass;
        this.httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
    }

    public MappingException(String exceptionMsg, Class<?> bindingClass, Class<?> bindingFromClass) {
        super(exceptionMsg, bindingClass);
        this.bindingFromClass = bindingFromClass;
        this.httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
    }

    public MappingException(String exceptionMsg, Class<?> bindingClass, HttpStatus httpStatus,
                            Class<?> bindingFromClass) {
        super(exceptionMsg, bindingClass, httpStatus);
        this.bindingFromClass = bindingFromClass;
    }

    @Override
    public String getMessage() {
        return new StringBuilder("Couldn't map data from ")
                .append(this.bindingFromClass.getName())
                .append(" to ")
                .append(this.bindingClass.getName())
                .append(" : ")
                .append(this.userExceptionMsg)
                .toString();
    }
}
