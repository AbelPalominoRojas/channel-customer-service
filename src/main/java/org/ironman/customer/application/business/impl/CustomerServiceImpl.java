package org.ironman.customer.application.business.impl;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.ironman.customer.application.business.CustomerService;
import org.ironman.customer.application.integration.partyreference.PartyReferenceClient;
import org.ironman.customer.application.integration.partyreference.model.PartyReferenceFilter;
import org.ironman.customer.application.mapper.CustomerMapper;
import org.ironman.customer.application.mapper.EnumConverter;
import org.ironman.customer.application.model.api.*;

@RequiredArgsConstructor
@ApplicationScoped
public class CustomerServiceImpl implements CustomerService {

  private final PartyReferenceClient partyReferenceClient;
  private final CustomerMapper customerMapper;

  @Override
  public Optional<CustomerResponse> getCustomerById(Long customerId) {
    return partyReferenceClient
        .retrievePartyReferenceDataDirectoryEntry(customerId)
        .map(customerMapper::toResponse);
  }

  @Override
  public CustomerListResponse getCustomers(CustomerFilter filter) {
    var partyFilter =
        new PartyReferenceFilter(
            filter.pageNumber(),
            filter.pageSize(),
            filter.identifierValue(),
            Optional.ofNullable(filter.customerType())
                .map(EnumConverter::mapToPartyTypeValue)
                .orElse(null),
            Optional.ofNullable(filter.residencyStatus())
                .map(EnumConverter::mapToResidencyStatusTypeValue)
                .orElse(null),
            Optional.ofNullable(filter.sortField())
                .map(EnumConverter::mapToPartyReferenceSortFieldValue)
                .orElse(null),
            Optional.ofNullable(filter.sortDirection())
                .map(EnumConverter::mapToSortDirectionValue)
                .orElse(null));

    var result = partyReferenceClient.retrievePartyReferenceDataDirectoryEntries(partyFilter);
    return customerMapper.toListResponse(result);
  }

  @Override
  public CustomerIdResponse createCustomer(CreateCustomerRequest createCustomerRequest) {
    var partyReferenceRequest = customerMapper.toRequest(createCustomerRequest);
    var result =
        partyReferenceClient.registerPartyReferenceDataDirectoryEntry(partyReferenceRequest);
    return customerMapper.toCustomerIdResponse(result);
  }

  @Override
  public CustomerIdResponse updateCustomer(
      Long customerId, CreateCustomerRequest createCustomerRequest) {
    var partyReferenceRequest = customerMapper.toRequest(createCustomerRequest);
    var result =
        partyReferenceClient.updatePartyReferenceDataDirectoryEntry(
            customerId, partyReferenceRequest);
    return customerMapper.toCustomerIdResponse(result);
  }
}
