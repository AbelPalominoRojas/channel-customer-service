package org.ironman.customer.expose.filter;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;
import lombok.RequiredArgsConstructor;
import org.ironman.customer.application.context.RequestContext;

@RequiredArgsConstructor
@Provider
public class RequestContextFilter implements ContainerRequestFilter {

  private static final String HEADER_REQUEST_ID = "Request-ID";

  private final RequestContext requestContext;

  @Override
  public void filter(ContainerRequestContext requestContext) {
    this.requestContext.init(requestContext.getHeaderString(HEADER_REQUEST_ID));
  }
}
