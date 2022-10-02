package com.oslash.integration.api.exception;

public class IntegrationException extends Throwable {

    public IntegrationException(String message) {
        super(message);
    }

    public IntegrationException(String message, Throwable cause) {
        super(message, cause);
    }

}