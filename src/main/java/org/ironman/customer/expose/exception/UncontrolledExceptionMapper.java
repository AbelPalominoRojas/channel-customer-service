package org.ironman.customer.expose.exception;

import static org.ironman.customer.application.exception.ExceptionConstants.ERROR_TYPE_TECHNICAL;
import static org.ironman.customer.application.exception.ExceptionConstants.MESSAGE_UNEXPECTED_ERROR;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;
import org.ironman.customer.application.model.api.ApiExceptionResponse;

@Slf4j
@Provider
public class UncontrolledExceptionMapper implements ExceptionMapper<Exception> {
  @Override
  public Response toResponse(Exception exception) {
    log.error("Uncontrolled Exception: {}", exception.getMessage(), exception);

    var response =
        new ApiExceptionResponse()
            .description(MESSAGE_UNEXPECTED_ERROR)
            .errorType(ERROR_TYPE_TECHNICAL);

    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
  }
}
