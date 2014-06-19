package com.rest.hgq.common.exceptions;

import javax.ws.rs.core.Response;

public class ValidationException extends HerenException {

    public ValidationException(String message) {
        this(message, null);
    }

    public ValidationException(Throwable cause) {
        this(cause.getMessage(), cause);
    }

    public ValidationException(String message, Throwable cause) {
        this(cause, Errors.errors(message));
    }

    public ValidationException(Throwable cause, Errors errors) {
        super(cause, Response.Status.PRECONDITION_FAILED, errors);
    }

}
