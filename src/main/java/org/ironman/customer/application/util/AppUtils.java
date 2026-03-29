package org.ironman.customer.application.util;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.ironman.customer.application.integration.partyreference.model.DirectoryEntryDate;
import org.ironman.customer.application.integration.partyreference.model.DirectoryEntryDateTypeValues;
import org.ironman.customer.application.integration.partyreference.model.PartyIdentificationTypeValues;
import org.ironman.customer.application.integration.partyreference.model.PartyName;
import org.ironman.customer.application.integration.partyreference.model.PartyNameTypeValues;
import org.ironman.customer.application.integration.partyreference.model.PartyTypeValues;
import org.ironman.customer.application.integration.partyreference.model.ResidencyStatusTypeValues;
import org.ironman.customer.application.model.api.CustomerTypeValues;
import org.ironman.customer.application.model.api.DocumentTypeValues;
import org.ironman.customer.application.model.api.ResidencyStatusValues;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AppUtils {

  public static String findNameByType(
      List<PartyName> partyNames, PartyNameTypeValues partyNameType) {
    if (partyNames == null || partyNameType == null) {
      return null;
    }

    return partyNames.stream()
        .filter(name -> name.getPartyNameType() == partyNameType)
        .findFirst()
        .map(PartyName::getPartyName)
        .orElse(null);
  }

  public static LocalDateTime findDateByType(
      List<DirectoryEntryDate> directoryEntryDates,
      DirectoryEntryDateTypeValues directoryEntryDateType) {
    if (directoryEntryDates == null || directoryEntryDateType == null) {
      return null;
    }

    return directoryEntryDates.stream()
        .filter(date -> date.getDirectoryEntryDateType() == directoryEntryDateType)
        .findFirst()
        .map(DirectoryEntryDate::getDirectoryEntryDate)
        .orElse(null);
  }

  public static DocumentTypeValues mapToDocumentTypeValue(
      PartyIdentificationTypeValues partyIdentificationType) {
    return mapEnumByToStringIgnoreCase(partyIdentificationType, DocumentTypeValues.values());
  }

  public static CustomerTypeValues mapToCustomerTypeValue(PartyTypeValues partyType) {
    return mapEnumByToStringIgnoreCase(partyType, CustomerTypeValues.values());
  }

  public static ResidencyStatusValues mapToResidencyStatusValue(
      ResidencyStatusTypeValues residencyStatus) {
    return mapEnumByToStringIgnoreCase(residencyStatus, ResidencyStatusValues.values());
  }

  private static <S extends Enum<S>, T extends Enum<T>> T mapEnumByToStringIgnoreCase(
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
