package org.ironman.customer.application.exception;

import lombok.*;
import org.ironman.customer.application.model.api.ApiExceptionResponse;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExternalException extends RuntimeException {
  private int httpStatus;
  private ApiExceptionResponse errorResponse;
}
