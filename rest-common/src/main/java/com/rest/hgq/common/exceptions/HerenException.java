package com.rest.hgq.common.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.List;

public abstract class HerenException extends WebApplicationException {
    protected Errors errors;

    public HerenException(Throwable cause, Response.Status status, Errors errors) {
        super(cause, Response.status(status).entity(errors).build());
        this.errors = errors;
    }

    public String getMessage() {
        return errors.getMessage();
    }

    public Errors getErrors() {
        return errors;
    }

    public List<Errors.Error> getErrorList() {
        return errors.getErrorList();
    }
}
