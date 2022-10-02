package com.oslash.integration.plugin.model;

import com.google.api.services.drive.model.File;

public class GDriveMetaData implements MetaData {
    private String id;
    private String fileName;
    private String name;
    private String extension;
    private String checkSum;
    private String mimeType;
    private long fileSize;

    public GDriveMetaData(String id, String fileName, String name, String extension, String checkSum, String mimeType, long fileSize) {
        this.id = id;
        this.fileName = fileName;
        this.name = name;
        this.extension = extension;
        this.checkSum = checkSum;
        this.mimeType = mimeType;
        this.fileSize = fileSize;
    }

    public GDriveMetaData(File file) {
        id = file.getId();

        fileName = file.getOriginalFilename();
        extension = file.getFileExtension();

        // Get file name without extension
        if(fileName != null && fileName.contains("\\.")) {
            name = fileName.replace("." + extension, "");
        }
        else if (file.getName() != null) {
            name = file.getName();
        }
        else {
            name = fileName;
        }

        checkSum = file.getMd5Checksum();
        mimeType = file.getMimeType();

        // Check if file size is present
        fileSize = file.getSize() != null ? file.getSize() : 0;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getExtension() {
        return extension;
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
    public long getSize() {
        return fileSize;
    }
}