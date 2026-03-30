package org.ironman.customer.application.business.impl;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.ironman.customer.application.business.CustomerService;
import org.ironman.customer.application.integration.partyreference.PartyReferenceClient;
import org.ironman.customer.application.mapper.CustomerMapper;
import org.ironman.customer.application.model.api.*;
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
  public CustomerListResponse getCustomers(
      String requestId,
      Integer pageNumber,
      Integer pageSize,
      String identifierValue,
      CustomerTypeValues customerType,
      ResidencyStatusValues residencyStatus,
      CustomerSortFieldValues sortField,
      SortDirectionUxValues sortDirection) {
    var partyType =
        Optional.ofNullable(customerType).map(AppUtils::mapToPartyTypeValue).orElse(null);
    var residencyStatusType =
        Optional.ofNullable(residencyStatus)
            .map(AppUtils::mapToResidencyStatusTypeValue)
            .orElse(null);
    var partySortField =
        Optional.ofNullable(sortField)
            .map(AppUtils::mapToPartyReferenceSortFieldValue)
            .orElse(null);
    var sortDirectionType =
        Optional.ofNullable(sortDirection).map(AppUtils::mapToSortDirectionValue).orElse(null);

    var result =
        partyReferenceClient.retrievePartyReferenceDataDirectoryEntries(
            requestId,
            pageNumber,
            pageSize,
            identifierValue,
            partyType,
            residencyStatusType,
            partySortField,
            sortDirectionType);

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
