package org.ironman.customer.application.exception;

import lombok.*;
import org.ironman.customer.application.model.api.ApiExceptionResponse;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class IntegrationException extends RuntimeException {
  private int httpStatus;
  private ApiExceptionResponse errorResponse;
}
