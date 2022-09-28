package com.oslash.integration.plugin.model.metadata;

import com.oslash.integration.plugin.constant.FileType;

/**
 * Model : Integration File MetaData
 *
 * @author amitchaudhari228@gmail.com
 * Sept-2022
 */

public interface MetaData {

    String getId();

    String getName();

    String getCheckSum();

    String getMimeType();

    FileType getFileType();

}