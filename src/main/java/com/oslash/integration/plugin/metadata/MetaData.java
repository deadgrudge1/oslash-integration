package com.oslash.integration.plugin.metadata;

public abstract class MetaData {
    protected String id;
    protected String name;
    protected String checkSum;

    protected MetaData(String id, String name, String checkSum) {
        this.id = id;
        this.name = name;
        this.checkSum = checkSum;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCheckSum() {
        return checkSum;
    }
}