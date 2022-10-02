package com.oslash.integration.executor.model.event;

import com.oslash.integration.plugin.constant.PluginType;

import java.util.UUID;

public class BenchMark {
    private String id;
    private String integrationId;
    private String basePath;

    private long bytesProcessed;

    private int itemsProcessed;
    private long itemsTimeTaken;
    private int itemsTimeTakenSeconds;

    private int batchSize;
    private int batchesProcessed;
    private long batchesTimeTaken;
    private int batchesTimeTakenSeconds;

    private float parallelPerformanceFactor;
    private float throughputKbps;

    private PluginType pluginType;

    public BenchMark(PluginType pluginType, String integrationId, int batchSize, String basePath) {
        this.id = UUID.randomUUID().toString().replace("-", "_").toUpperCase();
        this.pluginType = pluginType;
        this.integrationId = integrationId;
        this.batchSize = batchSize;
        this.basePath = basePath;
    }

    public void addItem(long bytesProcessed, long millis) {
        this.bytesProcessed = this.bytesProcessed + bytesProcessed;
        this.itemsProcessed++;

        itemsTimeTaken = itemsTimeTaken + millis;
        itemsTimeTakenSeconds = (int) (itemsTimeTaken / 1000);
    }

    public void completeBatch(long millis) {
        this.batchesProcessed++;

        batchesTimeTaken = batchesTimeTaken + millis;
        batchesTimeTakenSeconds = (int) batchesTimeTaken / 1000;

        computeStatistics();
    }

    private void computeStatistics() {
        if(itemsTimeTakenSeconds != 0) {
            this.parallelPerformanceFactor = (float) itemsTimeTaken / batchesTimeTaken;
        }

        if(batchesTimeTakenSeconds != 0 && batchesProcessed != 0) {
            throughputKbps =  ((float) (bytesProcessed / 1024))  / batchesTimeTakenSeconds;
        }
    }

    public String getId() {
        return id;
    }

    public String getIntegrationId() {
        return integrationId;
    }

    public int getItemsProcessed() {
        return itemsProcessed;
    }

    public PluginType getPluginType() {
        return pluginType;
    }

    public String getBasePath() {
        return basePath;
    }

    public int getBatchesProcessed() {
        return batchesProcessed;
    }

    public long getItemsTimeTaken() {
        return itemsTimeTaken;
    }

    public int getItemsTimeTakenSeconds() {
        return itemsTimeTakenSeconds;
    }

    public long getBatchesTimeTaken() {
        return batchesTimeTaken;
    }

    public int getBatchesTimeTakenSeconds() {
        return batchesTimeTakenSeconds;
    }

    public long getBytesProcessed() {
        return bytesProcessed;
    }

    public int getBatchSize() {
        return batchSize;
    }
}