package org.ironman.customer.application.integration.exception;

import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.rest.client.ext.ResponseExceptionMapper;
import org.ironman.customer.application.exception.ExternalException;
import org.ironman.customer.application.model.api.ApiExceptionResponse;

@Slf4j
@Provider
@Priority(Priorities.USER)
public class ClientExceptionResponseMapper implements ResponseExceptionMapper<ExternalException> {

  private static final int INTERNAL_SERVER_ERROR = 500;
  private static final String TECHNICAL_ERROR_TYPE = "TECHNICAL";
  private static final String FALLBACK_ERROR_DESCRIPTION =
      "External service returned an unexpected error payload";

  @Override
  public ExternalException toThrowable(Response response) {
    int httpStatus = resolveStatus(response);
    ApiExceptionResponse errorResponse = extractErrorResponse(response);

    return ExternalException.builder().httpStatus(httpStatus).errorResponse(errorResponse).build();
  }

  private int resolveStatus(Response response) {
    return response != null ? response.getStatus() : INTERNAL_SERVER_ERROR;
  }

  private ApiExceptionResponse extractErrorResponse(Response response) {
    if (response == null) {
      return buildFallbackErrorResponse();
    }

    try {
      response.bufferEntity();
      if (response.hasEntity()) {
        ApiExceptionResponse errorResponse = response.readEntity(ApiExceptionResponse.class);
        if (errorResponse != null) {
          return errorResponse;
        }
      }
    } catch (Exception exception) {
      log.error(FALLBACK_ERROR_DESCRIPTION, exception);
    }

    return buildFallbackErrorResponse();
  }

  private ApiExceptionResponse buildFallbackErrorResponse() {
    return new ApiExceptionResponse()
        .description(FALLBACK_ERROR_DESCRIPTION)
        .errorType(TECHNICAL_ERROR_TYPE);
  }
}
