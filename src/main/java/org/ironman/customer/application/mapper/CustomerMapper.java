package org.ironman.customer.application.mapper;

import static org.ironman.customer.application.integration.partyreference.model.DirectoryEntryDateTypeValues.FECHA_CREACION;
import static org.ironman.customer.application.integration.partyreference.model.DirectoryEntryDateTypeValues.FECHA_MODIFICACION;
import static org.ironman.customer.application.integration.partyreference.model.PartyNameTypeValues.APELLIDO_MATERNO;
import static org.ironman.customer.application.integration.partyreference.model.PartyNameTypeValues.APELLIDO_PATERNO;
import static org.ironman.customer.application.integration.partyreference.model.PartyNameTypeValues.NOMBRE;
import static org.ironman.customer.application.integration.partyreference.model.PartyNameTypeValues.NOMBRE_FANTASIA;
import static org.ironman.customer.application.integration.partyreference.model.PartyNameTypeValues.RAZON_SOCIAL;
import static org.ironman.customer.application.integration.partyreference.model.PartyTypeValues.PERSONA;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA_CDI)
public interface CustomerMapper {

  @Mapping(target = "customerId", source = "partyReference.partyId")
  @Mapping(
      target = "documentType",
      source = "partyReference.partyIdentification.partyIdentificationType")
  @Mapping(
      target = "documentNumber",
      source = "partyReference.partyIdentification.partyIdentification.identifierValue")
  @Mapping(target = "name", ignore = true)
  @Mapping(target = "paternalSurname", ignore = true)
  @Mapping(target = "maternalSurname", ignore = true)
  @Mapping(target = "tradeName", ignore = true)
  @Mapping(target = "customerType", source = "partyType")
  @Mapping(target = "residencyStatus", source = "residencyStatus")
  CustomerResponse toResponse(RetrievePartyReferenceDataDirectoryEntryResponse response);

  @Mapping(target = "customerId", source = "partyReference.partyId")
  @Mapping(
      target = "documentType",
      source = "partyReference.partyIdentification.partyIdentificationType")
  @Mapping(
      target = "documentNumber",
      source = "partyReference.partyIdentification.partyIdentification.identifierValue")
  @Mapping(target = "name", ignore = true)
  @Mapping(target = "paternalSurname", ignore = true)
  @Mapping(target = "maternalSurname", ignore = true)
  @Mapping(target = "tradeName", ignore = true)
  @Mapping(target = "customerType", source = "partyType")
  @Mapping(target = "residencyStatus", source = "residencyStatus")
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

    var partyType = EnumConverter.mapToPartyTypeValue(request.getCustomerType());
    var residencyStatus = EnumConverter.mapToResidencyStatusTypeValue(request.getResidencyStatus());
    var identificationType =
        EnumConverter.mapToPartyIdentificationTypeValue(request.getDocumentType());

    var identifier = new Identifier(request.getDocumentNumber());
    var partyIdentification = new PartyIdentification(identificationType, identifier);
    var partyReference =
        new PartyReference(partyIdentification, buildPartyNames(request, partyType));

    return new RegisterPartyReferenceDataDirectoryEntryRequest(
        partyReference, partyType, residencyStatus);
  }

  @AfterMapping
  default void populateCustomerFields(
      RetrievePartyReferenceDataDirectoryEntryResponse source,
      @MappingTarget CustomerResponse customer) {
    var partyReference = source.getPartyReference();
    if (partyReference != null) {
      var partyNames = partyReference.getPartyNames();
      if (partyNames != null) {
        boolean isPerson = PERSONA == source.getPartyType();
        customer.setName(findNameByType(partyNames, isPerson ? NOMBRE : RAZON_SOCIAL));
        customer.setPaternalSurname(findNameByType(partyNames, APELLIDO_PATERNO));
        customer.setMaternalSurname(findNameByType(partyNames, APELLIDO_MATERNO));
        customer.setTradeName(findNameByType(partyNames, NOMBRE_FANTASIA));
      }
    }

    var directoryEntryDates = source.getDirectoryEntryDates();
    if (directoryEntryDates != null) {
      customer.setCreatedAt(findDateByType(directoryEntryDates, FECHA_CREACION));
      customer.setUpdatedAt(findDateByType(directoryEntryDates, FECHA_MODIFICACION));
    }
  }

  @AfterMapping
  default void populateSummaryFields(
      PartyReferenceDataDirectoryEntry entry, @MappingTarget CustomerSummary summary) {
    var partyReference = entry.getPartyReference();
    if (partyReference != null) {
      var partyNames = partyReference.getPartyNames();
      if (partyNames != null) {
        boolean isPerson = PERSONA == entry.getPartyType();
        summary.setName(findNameByType(partyNames, isPerson ? NOMBRE : RAZON_SOCIAL));
        summary.setPaternalSurname(findNameByType(partyNames, APELLIDO_PATERNO));
        summary.setMaternalSurname(findNameByType(partyNames, APELLIDO_MATERNO));
        summary.setTradeName(findNameByType(partyNames, NOMBRE_FANTASIA));
      }
    }
  }

  default DocumentTypeValues toDocumentTypeValues(PartyIdentificationTypeValues type) {
    return EnumConverter.mapToDocumentTypeValue(type);
  }

  default CustomerTypeValues toCustomerTypeValues(PartyTypeValues type) {
    return EnumConverter.mapToCustomerTypeValue(type);
  }

  default ResidencyStatusValues toResidencyStatusValues(ResidencyStatusTypeValues status) {
    return EnumConverter.mapToResidencyStatusValue(status);
  }

  private List<PartyName> buildPartyNames(
      CreateCustomerRequest request, PartyTypeValues partyType) {
    boolean isPerson = PERSONA == partyType;
    var partyNames = new ArrayList<PartyName>();

    partyNames.add(new PartyName(isPerson ? NOMBRE : RAZON_SOCIAL, request.getName()));

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

    return partyNames;
  }

  private static String findNameByType(
      List<PartyName> partyNames, PartyNameTypeValues partyNameType) {
    return partyNames.stream()
        .filter(name -> name.getPartyNameType() == partyNameType)
        .findFirst()
        .map(PartyName::getPartyName)
        .orElse(null);
  }

  private static LocalDateTime findDateByType(
      List<DirectoryEntryDate> directoryEntryDates,
      DirectoryEntryDateTypeValues directoryEntryDateType) {
    return directoryEntryDates.stream()
        .filter(date -> date.getDirectoryEntryDateType() == directoryEntryDateType)
        .findFirst()
        .map(DirectoryEntryDate::getDirectoryEntryDate)
        .orElse(null);
  }
}
