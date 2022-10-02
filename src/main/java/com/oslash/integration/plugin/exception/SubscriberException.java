package com.oslash.integration.plugin.exception;

public class SubscriberException extends Throwable {

    public SubscriberException(String message, Throwable e) {
        super(message, e);
    }

    public SubscriberException(Throwable e) {
        super(e);
    }

}