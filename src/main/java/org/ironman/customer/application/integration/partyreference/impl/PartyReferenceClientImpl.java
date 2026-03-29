package org.ironman.customer.application.integration.partyreference.impl;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.ironman.customer.application.integration.partyreference.PartyReferenceClient;
import org.ironman.customer.application.integration.partyreference.model.RetrievePartyReferenceDataDirectoryEntryResponse;
import org.ironman.customer.application.integration.partyreference.restclient.PartyReferenceDataDirectoryProxy;

@Slf4j
@RequiredArgsConstructor
@ApplicationScoped
public class PartyReferenceClientImpl implements PartyReferenceClient {

  @RestClient private final PartyReferenceDataDirectoryProxy partyReferenceDataDirectoryProxy;

  @Override
  public Optional<RetrievePartyReferenceDataDirectoryEntryResponse>
      retrievePartyReferenceDataDirectoryEntry(
          String requestId, Long partyReferenceDataDirectoryEntryId) {
    var result =
        partyReferenceDataDirectoryProxy.retrievePartyReferenceDataDirectoryEntry(
            requestId, partyReferenceDataDirectoryEntryId);

    return Optional.ofNullable(result);
  }
}
