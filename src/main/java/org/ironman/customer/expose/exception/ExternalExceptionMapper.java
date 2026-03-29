package org.ironman.customer.expose.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;
import org.ironman.customer.application.exception.ExternalException;

@Slf4j
@Provider
public class ExternalExceptionMapper implements ExceptionMapper<ExternalException> {
  @Override
  public Response toResponse(ExternalException exception) {
    return Response.status(exception.getHttpStatus()).entity(exception.getErrorResponse()).build();
  }
}
