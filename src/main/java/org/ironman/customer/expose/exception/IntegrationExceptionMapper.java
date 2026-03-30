package org.ironman.customer.expose.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.ironman.customer.application.exception.IntegrationException;

@Provider
public class IntegrationExceptionMapper implements ExceptionMapper<IntegrationException> {
  @Override
  public Response toResponse(IntegrationException exception) {
    return Response.status(exception.getHttpStatus()).entity(exception.getErrorResponse()).build();
  }
}
