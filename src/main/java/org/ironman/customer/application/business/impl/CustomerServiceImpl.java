package org.ironman.customer.application.business.impl;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.ironman.customer.application.business.CustomerService;
import org.ironman.customer.application.integration.partyreference.PartyReferenceClient;
import org.ironman.customer.application.mapper.CustomerMapper;
import org.ironman.customer.application.model.api.CustomerResponse;

@RequiredArgsConstructor
@ApplicationScoped
public class CustomerServiceImpl implements CustomerService {

  private final PartyReferenceClient partyReferenceClient;
  private final CustomerMapper customerMapper;

  @Override
  public Optional<CustomerResponse> getCustomerById(String requestId, Long customerId) {
    return partyReferenceClient
        .retrievePartyReferenceDataDirectoryEntry(requestId, customerId)
        .map(customerMapper::toResponse);
  }
}
