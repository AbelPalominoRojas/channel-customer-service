package org.ironman.customer.application.context;

import jakarta.enterprise.context.RequestScoped;
import lombok.Getter;

@Getter
@RequestScoped
public class RequestContext {

  private String requestId;

  public void init(String requestId) {
    this.requestId = requestId;
  }
}
