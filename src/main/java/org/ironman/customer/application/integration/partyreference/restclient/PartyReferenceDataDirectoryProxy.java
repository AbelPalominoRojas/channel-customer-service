package org.ironman.customer.application.integration.partyreference.restclient;

import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.ironman.customer.application.integration.exception.IntegrationResponseExceptionMapper;
import org.ironman.customer.application.integration.partyreference.PartyReferenceDataDirectoryApi;

@RegisterRestClient(configKey = "business-party-reference")
@RegisterProvider(IntegrationResponseExceptionMapper.class)
public interface PartyReferenceDataDirectoryProxy extends PartyReferenceDataDirectoryApi {}
