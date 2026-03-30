package org.ironman.customer.application.model.api;

public record CustomerFilter(
    Integer pageNumber,
    Integer pageSize,
    String identifierValue,
    CustomerTypeValues customerType,
    ResidencyStatusValues residencyStatus,
    CustomerSortFieldValues sortField,
    SortDirectionUxValues sortDirection) {}
