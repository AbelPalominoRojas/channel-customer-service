package org.ironman.customer.expose.web;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.ironman.customer.application.business.CustomerService;
import org.ironman.customer.application.model.api.*;

@RequiredArgsConstructor
@ApplicationScoped
public class CustomersApiImpl implements CustomersApi {

  private final CustomerService customerService;

  @Override
  public Response createCustomer(String requestID, CreateCustomerRequest createCustomerRequest) {
    var result = customerService.createCustomer(requestID, createCustomerRequest);
    return Response.status(Response.Status.CREATED).entity(result).build();
  }

  @Override
  public Response getCustomerById(String requestID, Long customerId) {
    var result = customerService.getCustomerById(requestID, customerId);

    if (result.isEmpty()) {
      return Response.noContent().build();
    }

    return Response.ok().entity(result.get()).build();
  }

  @Override
  public Response getCustomers(
      String requestID,
      Integer pageNumber,
      Integer pageSize,
      String identifierValue,
      CustomerTypeValues customerType,
      ResidencyStatusValues residencyStatus,
      CustomerSortFieldValues sortField,
      SortDirectionUxValues sortDirection) {
    var result =
        customerService.getCustomers(
            requestID,
            pageNumber,
            pageSize,
            identifierValue,
            customerType,
            residencyStatus,
            sortField,
            sortDirection);
    return Response.ok().entity(result).build();
  }

  @Override
  public Response updateCustomer(
      String requestID, Long customerId, CreateCustomerRequest createCustomerRequest) {
    var result = customerService.updateCustomer(requestID, customerId, createCustomerRequest);
    return Response.ok().entity(result).build();
  }
}
