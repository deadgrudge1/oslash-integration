package com.oslash.integration.plugin.model;

/**
 * Model : Integration File MetaData
 *
 * @author amitchaudhari228@gmail.com
 * Sept-2022
 */

public interface MetaData {

    String getId();

    String getFileName();

    String getName();

    String getExtension();

    String getCheckSum();

    String getMimeType();

    long getSize();

}