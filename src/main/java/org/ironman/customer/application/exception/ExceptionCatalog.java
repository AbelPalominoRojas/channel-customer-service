package org.ironman.customer.application.exception;

import static org.ironman.customer.application.exception.ApplicationException.ExceptionType;
import static org.ironman.customer.application.exception.ExceptionConstants.COMPONENT_CUSTOMER_SERVICE;
import static org.ironman.customer.application.exception.ExceptionConstants.COMPONENT_EXTERNAL_SERVICE;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionCatalog {
  EXTERNAL_SERVICE_ERROR(
      "BPRDD0001",
      ExceptionType.INTERNAL_SERVER_ERROR,
      COMPONENT_EXTERNAL_SERVICE,
      "An unexpected error occurred in the external service."),
  APPLICATION_ERROR(
      "BPRDD0002",
      ExceptionType.INTERNAL_SERVER_ERROR,
      COMPONENT_CUSTOMER_SERVICE,
      "An unexpected error occurred, please try again later.");

  private final String code;
  private final ExceptionType exceptionType;
  private final String component;
  private final String message;

  public ApplicationException buildException(Object... args) {
    String formattedMessage = String.format(message, args);

    return ApplicationException.builder()
        .code(code)
        .exceptionType(exceptionType)
        .component(component)
        .message(formattedMessage)
        .build();
  }
}
