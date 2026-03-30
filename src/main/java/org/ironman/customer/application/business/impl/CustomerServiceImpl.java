package org.ironman.customer.application.business.impl;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.ironman.customer.application.business.CustomerService;
import org.ironman.customer.application.integration.partyreference.PartyReferenceClient;
import org.ironman.customer.application.integration.partyreference.model.PartyReferenceFilter;
import org.ironman.customer.application.mapper.CustomerMapper;
import org.ironman.customer.application.model.api.*;
import org.ironman.customer.application.model.api.CustomerFilter;
import org.ironman.customer.application.util.AppUtils;

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

  @Override
  public CustomerListResponse getCustomers(String requestId, CustomerFilter filter) {
    var partyFilter =
        new PartyReferenceFilter(
            filter.pageNumber(),
            filter.pageSize(),
            filter.identifierValue(),
            Optional.ofNullable(filter.customerType())
                .map(AppUtils::mapToPartyTypeValue)
                .orElse(null),
            Optional.ofNullable(filter.residencyStatus())
                .map(AppUtils::mapToResidencyStatusTypeValue)
                .orElse(null),
            Optional.ofNullable(filter.sortField())
                .map(AppUtils::mapToPartyReferenceSortFieldValue)
                .orElse(null),
            Optional.ofNullable(filter.sortDirection())
                .map(AppUtils::mapToSortDirectionValue)
                .orElse(null));

    var result =
        partyReferenceClient.retrievePartyReferenceDataDirectoryEntries(requestId, partyFilter);
    return customerMapper.toListResponse(result);
  }

  @Override
  public CustomerIdResponse createCustomer(
      String requestId, CreateCustomerRequest createCustomerRequest) {
    var partyReferenceRequest = customerMapper.toRequest(createCustomerRequest);
    var result =
        partyReferenceClient.registerPartyReferenceDataDirectoryEntry(
            requestId, partyReferenceRequest);
    return customerMapper.toCustomerIdResponse(result);
  }

  @Override
  public CustomerIdResponse updateCustomer(
      String requestId, Long customerId, CreateCustomerRequest createCustomerRequest) {
    var partyReferenceRequest = customerMapper.toRequest(createCustomerRequest);
    var result =
        partyReferenceClient.updatePartyReferenceDataDirectoryEntry(
            requestId, customerId, partyReferenceRequest);
    return customerMapper.toCustomerIdResponse(result);
  }
}
