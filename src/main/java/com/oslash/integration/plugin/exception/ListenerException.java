package com.oslash.integration.plugin.exception;

public class ListenerException extends Throwable {

    public ListenerException(String message) {
        super(message);
    }

    public ListenerException(Throwable cause) {
        super(cause);
    }

    public ListenerException(String message, Throwable cause) {
        super(message, cause);
    }
}