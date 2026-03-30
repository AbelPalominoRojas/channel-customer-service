package org.ironman.customer.application.integration.partyreference;

import java.util.Optional;
import org.ironman.customer.application.integration.partyreference.model.*;

public interface PartyReferenceClient {
  Optional<RetrievePartyReferenceDataDirectoryEntryResponse>
      retrievePartyReferenceDataDirectoryEntry(
          String requestId, Long partyReferenceDataDirectoryEntryId);

  RetrievePartyReferenceDataDirectoryEntryListResponse retrievePartyReferenceDataDirectoryEntries(
      String requestId, PartyReferenceFilter filter);

  RegisterPartyReferenceDataDirectoryEntryResponse registerPartyReferenceDataDirectoryEntry(
      String requestId, RegisterPartyReferenceDataDirectoryEntryRequest request);

  RegisterPartyReferenceDataDirectoryEntryResponse updatePartyReferenceDataDirectoryEntry(
      String requestId,
      Long partyReferenceDataDirectoryEntryId,
      RegisterPartyReferenceDataDirectoryEntryRequest request);
}
