package org.milestogo.framework;

import org.milestogo.exceptions.ViewException;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Created by shekhargulati on 21/03/14.
 */
@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {

    @Override
    public Response toResponse(NotFoundException exception) {
        System.out.println("In NotFoundException Mapper");
        return Response.status(Response.Status.NOT_FOUND).entity(new View("/404", exception.getMessage(), "error")).build();
    }
}
