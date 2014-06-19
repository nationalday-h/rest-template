package com.rest.hgq.common.exceptions;

import javax.ws.rs.core.Response;

public class BusinessException extends HerenException {

    public BusinessException(String message) {
        this(message, null);
    }

    public BusinessException(Throwable cause) {
        this(cause.getMessage(), cause);
    }

    public BusinessException(String message, Throwable cause) {
        super(cause, Response.Status.SERVICE_UNAVAILABLE, Errors.errors(message));
    }
}
