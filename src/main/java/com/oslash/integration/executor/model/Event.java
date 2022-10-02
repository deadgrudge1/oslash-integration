package com.oslash.integration.executor.model;

public interface Event {

    void start();

    void end(boolean success);

    boolean isSuccess();

}
