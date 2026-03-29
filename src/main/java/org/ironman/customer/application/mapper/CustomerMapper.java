package org.ironman.customer.application.mapper;

import static org.ironman.customer.application.integration.partyreference.model.DirectoryEntryDateTypeValues.FECHA_CREACION;
import static org.ironman.customer.application.integration.partyreference.model.DirectoryEntryDateTypeValues.FECHA_MODIFICACION;
import static org.ironman.customer.application.integration.partyreference.model.PartyNameTypeValues.APELLIDO_MATERNO;
import static org.ironman.customer.application.integration.partyreference.model.PartyNameTypeValues.APELLIDO_PATERNO;
import static org.ironman.customer.application.integration.partyreference.model.PartyNameTypeValues.NOMBRE;
import static org.ironman.customer.application.integration.partyreference.model.PartyNameTypeValues.NOMBRE_FANTASIA;
import static org.ironman.customer.application.integration.partyreference.model.PartyNameTypeValues.RAZON_SOCIAL;
import static org.ironman.customer.application.integration.partyreference.model.PartyTypeValues.PERSONA;
import static org.ironman.customer.application.util.AppUtils.findDateByType;
import static org.ironman.customer.application.util.AppUtils.findNameByType;

import java.util.Optional;
import org.ironman.customer.application.integration.partyreference.model.*;
import org.ironman.customer.application.model.api.CustomerResponse;
import org.ironman.customer.application.model.api.CustomerTypeValues;
import org.ironman.customer.application.model.api.DocumentTypeValues;
import org.ironman.customer.application.model.api.ResidencyStatusValues;
import org.ironman.customer.application.util.AppUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA_CDI)
public interface CustomerMapper {

  @Mapping(target = "customerId", source = "partyReference.partyId")
  @Mapping(
      target = "documentType",
      source = "partyReference.partyIdentification.partyIdentificationType",
      qualifiedByName = "mapDocumentType")
  @Mapping(
      target = "documentNumber",
      source = "partyReference.partyIdentification.partyIdentification.identifierValue")
  @Mapping(target = "name", ignore = true)
  @Mapping(target = "paternalSurname", ignore = true)
  @Mapping(target = "maternalSurname", ignore = true)
  @Mapping(target = "tradeName", ignore = true)
  @Mapping(target = "customerType", source = "partyType", qualifiedByName = "mapCustomerType")
  @Mapping(
      target = "residencyStatus",
      source = "residencyStatus",
      qualifiedByName = "mapResidencyStatus")
  CustomerResponse toResponse(RetrievePartyReferenceDataDirectoryEntryResponse response);

  @Named("mapDocumentType")
  default DocumentTypeValues mapDocumentType(PartyIdentificationTypeValues identificationType) {
    return Optional.ofNullable(identificationType)
        .map(AppUtils::mapToDocumentTypeValue)
        .orElse(null);
  }

  @Named("mapCustomerType")
  default CustomerTypeValues mapCustomerType(PartyTypeValues partyType) {
    return Optional.ofNullable(partyType).map(AppUtils::mapToCustomerTypeValue).orElse(null);
  }

  @Named("mapResidencyStatus")
  default ResidencyStatusValues mapResidencyStatus(ResidencyStatusTypeValues residencyStatus) {
    return Optional.ofNullable(residencyStatus)
        .map(AppUtils::mapToResidencyStatusValue)
        .orElse(null);
  }

  @AfterMapping
  default void populateCustomerFields(
      RetrievePartyReferenceDataDirectoryEntryResponse partyReferenceResponse,
      @MappingTarget CustomerResponse customer) {
    if (partyReferenceResponse == null || customer == null) {
      return;
    }

    var partyReference = partyReferenceResponse.getPartyReference();
    if (partyReference != null) {

      var partyNames = partyReference.getPartyNames();
      if (partyNames != null) {
        boolean isPerson = PERSONA == partyReferenceResponse.getPartyType();
        PartyNameTypeValues nameType = isPerson ? NOMBRE : RAZON_SOCIAL;
        customer.setName(findNameByType(partyNames, nameType));

        customer.setPaternalSurname(findNameByType(partyNames, APELLIDO_PATERNO));
        customer.setMaternalSurname(findNameByType(partyNames, APELLIDO_MATERNO));
        customer.setTradeName(findNameByType(partyNames, NOMBRE_FANTASIA));
      }
    }

    var directoryEntryDates = partyReferenceResponse.getDirectoryEntryDates();
    if (directoryEntryDates != null) {
      customer.setCreatedAt(findDateByType(directoryEntryDates, FECHA_CREACION));
      customer.setUpdatedAt(findDateByType(directoryEntryDates, FECHA_MODIFICACION));
    }
  }
}
