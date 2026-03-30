package org.ironman.customer.application.integration.partyreference;

import java.util.Optional;
import org.ironman.customer.application.integration.partyreference.model.*;

public interface PartyReferenceClient {
  Optional<RetrievePartyReferenceDataDirectoryEntryResponse>
      retrievePartyReferenceDataDirectoryEntry(Long partyReferenceDataDirectoryEntryId);

  RetrievePartyReferenceDataDirectoryEntryListResponse retrievePartyReferenceDataDirectoryEntries(
      PartyReferenceFilter filter);

  RegisterPartyReferenceDataDirectoryEntryResponse registerPartyReferenceDataDirectoryEntry(
      RegisterPartyReferenceDataDirectoryEntryRequest request);

  RegisterPartyReferenceDataDirectoryEntryResponse updatePartyReferenceDataDirectoryEntry(
      Long partyReferenceDataDirectoryEntryId,
      RegisterPartyReferenceDataDirectoryEntryRequest request);
}
