package com.sismics.rest.exception;

import javax.json.Json;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 * Unauthorized access in case of expired or invalid access token
 *
 */
public class UnauthorizedClientException extends WebApplicationException {

    public UnauthorizedClientException() {
        super(Response.status(Status.UNAUTHORIZED).entity(Json.createObjectBuilder()
            .add("type", "UnauthorizedError")
            .add("message", "UNAUTHORIZED").build()).build());
    }
}
