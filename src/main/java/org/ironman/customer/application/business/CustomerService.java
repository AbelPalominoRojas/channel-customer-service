package org.ironman.customer.application.business;

import java.util.Optional;
import org.ironman.customer.application.model.api.*;
import org.ironman.customer.application.model.api.CustomerFilter;

public interface CustomerService {
  Optional<CustomerResponse> getCustomerById(String requestId, Long customerId);

  CustomerListResponse getCustomers(String requestId, CustomerFilter filter);

  CustomerIdResponse createCustomer(String requestId, CreateCustomerRequest createCustomerRequest);

  CustomerIdResponse updateCustomer(
      String requestId, Long customerId, CreateCustomerRequest createCustomerRequest);
}
