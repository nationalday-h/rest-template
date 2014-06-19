package com.rest.hgq.common.exceptions;

import javax.ws.rs.core.Response;

public class SystemException extends HerenException {
    public SystemException(String message) {
        this(message, null);
    }

    public SystemException(Throwable cause) {
        this(cause.getMessage(), cause);
    }

    public SystemException(String message, Throwable cause) {
        super(cause, Response.Status.INTERNAL_SERVER_ERROR, Errors.errors(message));
    }
}
