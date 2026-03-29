package org.ironman.customer.application.business;

import java.util.Optional;
import org.ironman.customer.application.model.api.CustomerResponse;

public interface CustomerService {
  Optional<CustomerResponse> getCustomerById(String requestId, Long customerId);
}
