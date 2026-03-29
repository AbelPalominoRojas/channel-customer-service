package org.ironman.customer.expose.exception;

import static org.ironman.customer.application.exception.ApplicationException.ExceptionType;
import static org.ironman.customer.application.exception.ExceptionConstants.*;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.List;
import org.ironman.customer.application.exception.ApplicationException;
import org.ironman.customer.application.model.api.ApiExceptionDetail;
import org.ironman.customer.application.model.api.ApiExceptionResponse;

@Provider
public class ApplicationExceptionMapper implements ExceptionMapper<ApplicationException> {
  @Override
  public Response toResponse(ApplicationException exception) {
    var detail = createApiExceptionDetail(exception);
    ApiExceptionResponse apiException = createExceptionResponse(exception, List.of(detail));
    Response.Status status = mapToJaxRsExceptionType(exception.getExceptionType());

    return Response.status(status).entity(apiException).build();
  }

  private static ApiExceptionDetail createApiExceptionDetail(ApplicationException exception) {
    return new ApiExceptionDetail()
        .code(exception.getCode())
        .component(exception.getComponent())
        .description(exception.getMessage());
  }

  private ApiExceptionResponse createExceptionResponse(
      ApplicationException exception, List<ApiExceptionDetail> details) {
    String description = descriptionFromExceptionType(exception.getExceptionType());
    String errorType = errorTypeFromExceptionType(exception.getExceptionType());

    return new ApiExceptionResponse()
        .description(description)
        .errorType(errorType)
        .exceptionDetails(details);
  }

  private String descriptionFromExceptionType(ExceptionType exceptionType) {
    return switch (exceptionType) {
      case BAD_REQUEST -> MESSAGE_INVALID_INPUT_DATA;
      case NOT_FOUND -> MESSAGE_RESOURCE_NOT_FOUND;
      case CONFLICT -> MESSAGE_BUSINESS_RULE_VIOLATION;
      case INTERNAL_SERVER_ERROR -> MESSAGE_INTERNAL_ERROR;
      default -> MESSAGE_UNEXPECTED_ERROR;
    };
  }

  private String errorTypeFromExceptionType(ExceptionType exceptionType) {
    return switch (exceptionType) {
      case BAD_REQUEST, NOT_FOUND, CONFLICT -> ERROR_TYPE_FUNCTIONAL;
      case INTERNAL_SERVER_ERROR -> ERROR_TYPE_TECHNICAL;
      default -> "UNKNOWN";
    };
  }

  private Response.Status mapToJaxRsExceptionType(ExceptionType exceptionType) {
    return switch (exceptionType) {
      case BAD_REQUEST -> Response.Status.BAD_REQUEST;
      case NOT_FOUND -> Response.Status.NOT_FOUND;
      case CONFLICT -> Response.Status.CONFLICT;
      default -> Response.Status.INTERNAL_SERVER_ERROR;
    };
  }
}
