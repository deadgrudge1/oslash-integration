package com.oslash.integration.executor.model.event;

import com.oslash.integration.executor.model.Event;
import com.oslash.integration.plugin.constant.PluginType;
import com.oslash.integration.plugin.model.MetaData;
import com.oslash.integration.plugin.service.reader.DataReader;

import java.util.Date;
import java.util.UUID;

public class FileEvent implements Event {
    private String id;
    private String integrationId;

    private PluginType pluginType;

    private boolean success;
    private Date startTime;
    private Date endTime;
    private String batchId;
    private long timeTaken;

    private String fileId;
    private String fileName;
    private String savedFileName;
    private String fileExtension;
    private String fileChecksum;
    private long fileSizeBytesProcessed;
    private boolean isAlreadyPresent;

    private String basePath;
    private String directory;
    private String absoluteFilePath;

    private MetaData metaData;
    private transient DataReader dataReader;

    public FileEvent(PluginType pluginType, MetaData metaData, DataReader dataReader) {
        this.pluginType = pluginType;
        this.metaData = metaData;
        this.dataReader = dataReader;

        this.setFileMetaData(metaData);

        id = UUID.randomUUID().toString().replace("-", "_").toUpperCase();
        success = false;
        startTime = new Date();
        endTime = new Date();
    }

    private void setFileMetaData(MetaData metaData) {
        fileId = metaData.getId();
        fileName = metaData.getFileName();
        fileChecksum = metaData.getCheckSum();

        fileName = metaData.getFileName();
        fileExtension = metaData.getExtension();

        // Default file extension - PDF
        savedFileName = fileId;
        if(fileExtension == null || fileExtension.isEmpty()) {
            savedFileName = savedFileName + "." + "pdf";
        }
        else {
            savedFileName = savedFileName + "." + fileExtension;
        }

        directory = basePath;
        absoluteFilePath = savedFileName;
    }

    public FileEvent withIntegrationId(String integrationId) {
        this.integrationId = integrationId;
        return this;
    }

    public FileEvent withBasePath(String downloadBasePath) {
        if(basePath == null) {
            basePath = downloadBasePath;
            directory = downloadBasePath;
        }

        absoluteFilePath = basePath + "/" + savedFileName;

        return this;
    }

    public void setFileSizeBytesProcessed(long fileSizeBytesProcessed) {
        this.fileSizeBytesProcessed = fileSizeBytesProcessed;
    }

    public void setAlreadyPresent(boolean alreadyPresent) {
        isAlreadyPresent = alreadyPresent;
    }

    @Override
    public void start() {
        this.success = false;
        this.startTime = new Date();
        this.endTime = new Date();
    }

    @Override
    public void end(boolean success) {
        this.success = success;
        this.endTime = new Date();

        this.timeTaken = this.endTime.getTime() - this.startTime.getTime();
    }

    @Override
    public boolean isSuccess() {
        return success;
    }

    public String getFileId() {
        return fileId;
    }

    public String getFileChecksum() {
        return fileChecksum;
    }

    public String getBasePath() {
        return basePath;
    }

    public long getFileSizeBytesProcessed() {
        return fileSizeBytesProcessed;
    }

    public MetaData getMetaData() {
        return metaData;
    }

    public long getTimeTaken() {
        return timeTaken;
    }

    public DataReader getDataReader() {
        return dataReader;
    }

    public String getIntegrationId() {
        return integrationId;
    }

    public boolean isAlreadyPresent() {
        return isAlreadyPresent;
    }

    public String getAbsoluteFilePath() {
        if(basePath == null) {
            absoluteFilePath = System.getProperty("user.home") + "/" + savedFileName;
        }

        return absoluteFilePath;
    }
}