package org.ironman.customer.application.integration.partyreference;

import java.util.Optional;
import org.ironman.customer.application.integration.partyreference.model.*;

public interface PartyReferenceClient {
  Optional<RetrievePartyReferenceDataDirectoryEntryResponse>
      retrievePartyReferenceDataDirectoryEntry(
          String requestId, Long partyReferenceDataDirectoryEntryId);

  RetrievePartyReferenceDataDirectoryEntryListResponse retrievePartyReferenceDataDirectoryEntries(
      String requestId,
      Integer pageNumber,
      Integer pageSize,
      String identifierValue,
      PartyTypeValues partyType,
      ResidencyStatusTypeValues residencyStatus,
      PartyReferenceSortFieldValues sortField,
      SortDirectionValues sortDirection);

  RegisterPartyReferenceDataDirectoryEntryResponse registerPartyReferenceDataDirectoryEntry(
      String requestId, RegisterPartyReferenceDataDirectoryEntryRequest Request);

  RegisterPartyReferenceDataDirectoryEntryResponse updatePartyReferenceDataDirectoryEntry(
      String requestId,
      Long partyReferenceDataDirectoryEntryId,
      RegisterPartyReferenceDataDirectoryEntryRequest request);
}
