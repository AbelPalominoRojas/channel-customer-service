package org.ironman.customer.application.integration.partyreference;

import java.util.Optional;
import org.ironman.customer.application.integration.partyreference.model.RetrievePartyReferenceDataDirectoryEntryResponse;

public interface PartyReferenceClient {
  Optional<RetrievePartyReferenceDataDirectoryEntryResponse>
      retrievePartyReferenceDataDirectoryEntry(
          String requestId, Long partyReferenceDataDirectoryEntryId);
}
