package com.oslash.integration.plugin.model;

import java.util.List;

public class MetaDataPage {
    private List<MetaData> metaDataList;
    private String nextPageToken;

    public MetaDataPage(List<MetaData> metaDataList, String nextPageToken) {
        this.metaDataList = metaDataList;
        this.nextPageToken = nextPageToken;
    }

    public List<MetaData> getMetaDataList() {
        return metaDataList;
    }

    public String getNextPageToken() {
        return nextPageToken;
    }

    public boolean isEmpty() {
        return metaDataList == null || metaDataList.isEmpty();
    }
}