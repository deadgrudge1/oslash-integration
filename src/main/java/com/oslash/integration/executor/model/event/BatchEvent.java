package com.oslash.integration.executor.model.event;

import com.oslash.integration.executor.model.Event;
import com.oslash.integration.plugin.constant.PluginType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class BatchEvent implements Event {
    private String id;
    private boolean success;
    private Date startTime;
    private Date endTime;
    private long timeTaken;

    private PluginType pluginType;
    private String basePath;
    private String integrationId;
    private String jobId;
    private int batchSize;

    private int itemCount;
    private List<FileEvent> fileEventList;

    public BatchEvent(PluginType pluginType, String basePath, String integrationId, String jobId) {
        this.pluginType = pluginType;
        this.basePath = basePath;
        this.integrationId = integrationId;
        this.jobId = jobId;

        id = UUID.randomUUID().toString().replace("-", "_").toUpperCase();
        this.start();
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

    public void addFileEvent(FileEvent fileEvent) {
        if(fileEventList == null) {
            fileEventList = new ArrayList<>();
        }

        fileEventList.add(fileEvent);

        itemCount++;
        batchSize++;
    }

    public String getBasePath() {
        return basePath;
    }

    public String getJobId() {
        return jobId;
    }

    public String getIntegrationId() {
        return integrationId;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public List<FileEvent> getFileEventList() {
        return fileEventList;
    }

    public long getTimeTaken() {
        return timeTaken;
    }
}