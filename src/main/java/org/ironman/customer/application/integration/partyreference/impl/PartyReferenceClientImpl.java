package org.ironman.customer.application.integration.partyreference.impl;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.ironman.customer.application.context.RequestContext;
import org.ironman.customer.application.integration.partyreference.PartyReferenceClient;
import org.ironman.customer.application.integration.partyreference.model.*;
import org.ironman.customer.application.integration.partyreference.restclient.PartyReferenceDataDirectoryProxy;

@Slf4j
@RequiredArgsConstructor
@ApplicationScoped
public class PartyReferenceClientImpl implements PartyReferenceClient {

  @RestClient private final PartyReferenceDataDirectoryProxy partyReferenceDataDirectoryProxy;
  private final RequestContext requestContext;

  @Override
  public Optional<RetrievePartyReferenceDataDirectoryEntryResponse>
      retrievePartyReferenceDataDirectoryEntry(Long partyReferenceDataDirectoryEntryId) {
    var result =
        partyReferenceDataDirectoryProxy.retrievePartyReferenceDataDirectoryEntry(
            requestContext.getRequestId(), partyReferenceDataDirectoryEntryId);
    return Optional.ofNullable(result);
  }

  @Override
  public RetrievePartyReferenceDataDirectoryEntryListResponse
      retrievePartyReferenceDataDirectoryEntries(PartyReferenceFilter filter) {
    return partyReferenceDataDirectoryProxy.retrievePartyReferenceDataDirectoryEntries(
        requestContext.getRequestId(),
        filter.pageNumber(),
        filter.pageSize(),
        filter.identifierValue(),
        filter.partyType(),
        filter.residencyStatus(),
        filter.sortField(),
        filter.sortDirection());
  }

  @Override
  public RegisterPartyReferenceDataDirectoryEntryResponse registerPartyReferenceDataDirectoryEntry(
      RegisterPartyReferenceDataDirectoryEntryRequest request) {
    return partyReferenceDataDirectoryProxy.registerPartyReferenceDataDirectoryEntry(
        requestContext.getRequestId(), request);
  }

  @Override
  public RegisterPartyReferenceDataDirectoryEntryResponse updatePartyReferenceDataDirectoryEntry(
      Long partyReferenceDataDirectoryEntryId,
      RegisterPartyReferenceDataDirectoryEntryRequest request) {
    return partyReferenceDataDirectoryProxy.updatePartyReferenceDataDirectoryEntry(
        requestContext.getRequestId(), partyReferenceDataDirectoryEntryId, request);
  }
}
