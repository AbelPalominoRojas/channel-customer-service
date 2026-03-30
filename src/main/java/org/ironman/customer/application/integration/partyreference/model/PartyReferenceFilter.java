package org.ironman.customer.application.integration.partyreference.model;

public record PartyReferenceFilter(
    Integer pageNumber,
    Integer pageSize,
    String identifierValue,
    PartyTypeValues partyType,
    ResidencyStatusTypeValues residencyStatus,
    PartyReferenceSortFieldValues sortField,
    SortDirectionValues sortDirection) {}
