package com.oslash.integration.plugin.service.listener;

import com.oslash.integration.executor.model.event.FileEvent;
import com.oslash.integration.executor.service.queue.Queue;
import com.oslash.integration.plugin.constant.PluginType;
import com.oslash.integration.plugin.exception.DataReaderException;
import com.oslash.integration.plugin.exception.ListenerException;
import com.oslash.integration.plugin.model.MetaData;
import com.oslash.integration.plugin.service.reader.DataReader;

import java.util.List;
import java.util.stream.Collectors;

public class GDriveFileChangesListener implements Listener {
    private String startPageToken;
    private PluginType pluginType;
    private DataReader dataReader;
    private Queue<FileEvent> queue;

    public GDriveFileChangesListener(PluginType pluginType, DataReader dataReader, Queue<FileEvent> queue) throws ListenerException {
        try {
            this.startPageToken = dataReader.newStartPageToken();
        } catch (DataReaderException e) {
            throw new ListenerException(e);
        }

        this.pluginType = pluginType;
        this.dataReader = dataReader;
        this.queue = queue;
    }

    @Override
    public void onChange() throws ListenerException {
        // Get Changes
        List<MetaData> changedFilesMetadataList;
        try {
            if(startPageToken == null) {
                startPageToken = dataReader.newStartPageToken();
            }

            changedFilesMetadataList = dataReader.getChanges(startPageToken);
        } catch (DataReaderException e) {
            throw new ListenerException(e);
        }

        // Changes found : Add metadata list to integration processor queue
        if(changedFilesMetadataList != null && !changedFilesMetadataList.isEmpty()) {
            queue.addAll(
                    changedFilesMetadataList
                            .stream()
                            .map(metaData -> new FileEvent(pluginType, metaData, dataReader))
                            .collect(Collectors.toList())
            );

            try {
                startPageToken = dataReader.newStartPageToken();
            } catch (DataReaderException e) {
                throw new ListenerException(e);
            }
        }
    }

}