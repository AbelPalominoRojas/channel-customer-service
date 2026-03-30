package org.ironman.customer.application.business;

import java.util.Optional;
import org.ironman.customer.application.model.api.*;
import org.ironman.customer.application.model.api.CustomerFilter;

public interface CustomerService {
  Optional<CustomerResponse> getCustomerById(Long customerId);

  CustomerListResponse getCustomers(CustomerFilter filter);

  CustomerIdResponse createCustomer(CreateCustomerRequest createCustomerRequest);

  CustomerIdResponse updateCustomer(Long customerId, CreateCustomerRequest createCustomerRequest);
}
