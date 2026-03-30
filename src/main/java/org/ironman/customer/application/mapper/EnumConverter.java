package org.ironman.customer.application.mapper;

import java.util.Arrays;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.ironman.customer.application.integration.partyreference.model.PartyIdentificationTypeValues;
import org.ironman.customer.application.integration.partyreference.model.PartyReferenceSortFieldValues;
import org.ironman.customer.application.integration.partyreference.model.PartyTypeValues;
import org.ironman.customer.application.integration.partyreference.model.ResidencyStatusTypeValues;
import org.ironman.customer.application.integration.partyreference.model.SortDirectionValues;
import org.ironman.customer.application.model.api.CustomerSortFieldValues;
import org.ironman.customer.application.model.api.CustomerTypeValues;
import org.ironman.customer.application.model.api.DocumentTypeValues;
import org.ironman.customer.application.model.api.ResidencyStatusValues;
import org.ironman.customer.application.model.api.SortDirectionUxValues;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EnumConverter {

  public static DocumentTypeValues mapToDocumentTypeValue(
      PartyIdentificationTypeValues partyIdentificationType) {
    return mapEnumByNameIgnoreCase(partyIdentificationType, DocumentTypeValues.values());
  }

  public static CustomerTypeValues mapToCustomerTypeValue(PartyTypeValues partyType) {
    return mapEnumByNameIgnoreCase(partyType, CustomerTypeValues.values());
  }

  public static ResidencyStatusValues mapToResidencyStatusValue(
      ResidencyStatusTypeValues residencyStatus) {
    return mapEnumByNameIgnoreCase(residencyStatus, ResidencyStatusValues.values());
  }

  public static PartyIdentificationTypeValues mapToPartyIdentificationTypeValue(
      DocumentTypeValues documentType) {
    return mapEnumByNameIgnoreCase(documentType, PartyIdentificationTypeValues.values());
  }

  public static PartyTypeValues mapToPartyTypeValue(CustomerTypeValues customerType) {
    return mapEnumByNameIgnoreCase(customerType, PartyTypeValues.values());
  }

  public static ResidencyStatusTypeValues mapToResidencyStatusTypeValue(
      ResidencyStatusValues residencyStatus) {
    return mapEnumByNameIgnoreCase(residencyStatus, ResidencyStatusTypeValues.values());
  }

  public static PartyReferenceSortFieldValues mapToPartyReferenceSortFieldValue(
      CustomerSortFieldValues sortField) {
    return switch (sortField) {
      case DOCUMENT_NUMBER -> PartyReferenceSortFieldValues.IDENTIFIER_VALUE;
      case CUSTOMER_TYPE -> PartyReferenceSortFieldValues.PARTY_TYPE;
      case RESIDENCY_STATUS -> PartyReferenceSortFieldValues.RESIDENCY_STATUS;
    };
  }

  public static SortDirectionValues mapToSortDirectionValue(SortDirectionUxValues sortDirection) {
    return mapEnumByNameIgnoreCase(sortDirection, SortDirectionValues.values());
  }

  private static <S extends Enum<S>, T extends Enum<T>> T mapEnumByNameIgnoreCase(
      S source, T[] candidates) {
    if (source == null || candidates == null) {
      return null;
    }

    return Arrays.stream(candidates)
        .filter(candidate -> candidate.name().equalsIgnoreCase(source.name()))
        .findFirst()
        .orElse(null);
  }
}
