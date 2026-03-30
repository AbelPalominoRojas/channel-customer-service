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

import java.util.ArrayList;
import java.util.Optional;
import org.ironman.customer.application.integration.partyreference.model.*;
import org.ironman.customer.application.model.api.CreateCustomerRequest;
import org.ironman.customer.application.model.api.CustomerIdResponse;
import org.ironman.customer.application.model.api.CustomerListResponse;
import org.ironman.customer.application.model.api.CustomerResponse;
import org.ironman.customer.application.model.api.CustomerSummary;
import org.ironman.customer.application.model.api.CustomerTypeValues;
import org.ironman.customer.application.model.api.DocumentTypeValues;
import org.ironman.customer.application.model.api.PaginationUx;
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
  CustomerSummary toSummary(PartyReferenceDataDirectoryEntry entry);

  PaginationUx toPaginationUx(Pagination pagination);

  CustomerListResponse toListResponse(
      RetrievePartyReferenceDataDirectoryEntryListResponse response);

  @Mapping(target = "customerId", source = "partyReference.partyId")
  CustomerIdResponse toCustomerIdResponse(
      RegisterPartyReferenceDataDirectoryEntryResponse response);

  default RegisterPartyReferenceDataDirectoryEntryRequest toRequest(CreateCustomerRequest request) {
    if (request == null) {
      return null;
    }

    var partyType =
        Optional.ofNullable(request.getCustomerType())
            .map(AppUtils::mapToPartyTypeValue)
            .orElse(null);

    var residencyStatus =
        Optional.ofNullable(request.getResidencyStatus())
            .map(AppUtils::mapToResidencyStatusTypeValue)
            .orElse(null);

    var identificationType =
        Optional.ofNullable(request.getDocumentType())
            .map(AppUtils::mapToPartyIdentificationTypeValue)
            .orElse(null);

    var identifier = new Identifier(request.getDocumentNumber());
    var partyIdentification = new PartyIdentification(identificationType, identifier);

    boolean isPerson = PERSONA == partyType;
    var partyNames = new ArrayList<PartyName>();

    var mainNameType = isPerson ? NOMBRE : RAZON_SOCIAL;
    partyNames.add(new PartyName(mainNameType, request.getName()));

    if (isPerson) {
      if (request.getPaternalSurname() != null) {
        partyNames.add(new PartyName(APELLIDO_PATERNO, request.getPaternalSurname()));
      }
      if (request.getMaternalSurname() != null) {
        partyNames.add(new PartyName(APELLIDO_MATERNO, request.getMaternalSurname()));
      }
    } else {
      if (request.getTradeName() != null) {
        partyNames.add(new PartyName(NOMBRE_FANTASIA, request.getTradeName()));
      }
    }

    var partyReference = new PartyReference(partyIdentification, partyNames);

    return new RegisterPartyReferenceDataDirectoryEntryRequest(
        partyReference, partyType, residencyStatus);
  }

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

  @AfterMapping
  default void populateSummaryFields(
      PartyReferenceDataDirectoryEntry entry, @MappingTarget CustomerSummary summary) {
    if (entry == null || summary == null) {
      return;
    }

    var partyReference = entry.getPartyReference();
    if (partyReference != null) {
      var partyNames = partyReference.getPartyNames();
      if (partyNames != null) {
        boolean isPerson = PERSONA == entry.getPartyType();
        PartyNameTypeValues nameType = isPerson ? NOMBRE : RAZON_SOCIAL;
        summary.setName(findNameByType(partyNames, nameType));

        summary.setPaternalSurname(findNameByType(partyNames, APELLIDO_PATERNO));
        summary.setMaternalSurname(findNameByType(partyNames, APELLIDO_MATERNO));
        summary.setTradeName(findNameByType(partyNames, NOMBRE_FANTASIA));
      }
    }
  }
}
