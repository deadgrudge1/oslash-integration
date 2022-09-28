package com.oslash.integration.api.service;

import com.oslash.integration.api.exception.IntegrationException;

public interface IntegrationService {

    void integrateDrive() throws IntegrationException;

}