package com.oslash.integration.plugin.model.metadata;

import com.google.api.services.drive.model.File;
import com.oslash.integration.plugin.constant.FileType;
import com.oslash.integration.plugin.model.metadata.MetaData;

public class GDriveMetaData implements MetaData {
    private String id;
    private String name;
    private String checkSum;
    private String mimeType;
    private FileType fileType;

    public GDriveMetaData(File file) {
        id = file.getId();
        name = file.getName();
        checkSum = file.getMd5Checksum();
        mimeType = file.getMimeType();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getCheckSum() {
        return checkSum;
    }

    @Override
    public String getMimeType() {
        return mimeType;
    }

    @Override
    public FileType getFileType() {
        return fileType;
    }
}