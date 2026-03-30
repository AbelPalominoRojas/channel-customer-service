package org.ironman.customer.application.business;

import java.util.Optional;
import org.ironman.customer.application.model.api.*;

public interface CustomerService {
  Optional<CustomerResponse> getCustomerById(String requestId, Long customerId);

  CustomerListResponse getCustomers(
      String requestId,
      Integer pageNumber,
      Integer pageSize,
      String identifierValue,
      CustomerTypeValues customerType,
      ResidencyStatusValues residencyStatus,
      CustomerSortFieldValues sortField,
      SortDirectionUxValues sortDirection);

  CustomerIdResponse createCustomer(String requestId, CreateCustomerRequest createCustomerRequest);

  CustomerIdResponse updateCustomer(
      String requestId, Long customerId, CreateCustomerRequest createCustomerRequest);
}
