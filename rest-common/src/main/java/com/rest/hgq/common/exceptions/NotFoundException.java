package com.rest.hgq.common.exceptions;

import javax.ws.rs.core.Response;

public class NotFoundException extends HerenException {

    public NotFoundException(String message) {
        this(message, null);
    }

    public NotFoundException(Throwable cause) {
        this(cause.getMessage(), cause);
    }

    public NotFoundException(String message, Throwable cause) {
        super(cause, Response.Status.NOT_FOUND, Errors.errors(message));
    }
}
