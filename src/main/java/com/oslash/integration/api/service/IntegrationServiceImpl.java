package com.oslash.integration.api.service;

import com.oslash.integration.api.exception.IntegrationException;
import com.oslash.integration.executor.constant.ExecutorConstant;
import com.oslash.integration.executor.model.event.FileEvent;
import com.oslash.integration.executor.singleton.FileQueueHandler;
import com.oslash.integration.plugin.exception.DataReaderException;
import com.oslash.integration.plugin.exception.PluginException;
import com.oslash.integration.plugin.Plugin;
import com.oslash.integration.plugin.factory.PluginFactory;
import com.oslash.integration.plugin.model.MetaDataPage;
import com.oslash.integration.plugin.service.reader.DataReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class IntegrationServiceImpl implements IntegrationService {

    @Autowired
    private PluginFactory pluginFactory;

    /**
     * Service - Integrate all files and spawn Listener for changes in source
     * @throws IntegrationException
     */
    public void integrateDrive() throws IntegrationException {
        Plugin drivePlugin = pluginFactory.createDrivePlugin();

        try {
            drivePlugin.connect("");
            DataReader dataReader = drivePlugin.getDataReader();

            // Get MetaData page by page
            MetaDataPage metaDataPage;
            String nextPageToken = null;
            do {
                metaDataPage = dataReader.getMetaData(nextPageToken);

                // Add MetaData Page to File Processor Queue
                if(!metaDataPage.isEmpty()) {

                    FileQueueHandler.getInstance().driveFileQueue().processAll(
                            metaDataPage.getMetaDataList()
                                    .stream()
                                    .map(metaData -> new FileEvent(drivePlugin.getType(), metaData, dataReader).withBasePath(ExecutorConstant.BASE_DOWNLOAD_URL))
                                    .collect(Collectors.toList())
                    );

                }

                // Scroll to next page while no more pages are available
                nextPageToken = metaDataPage.getNextPageToken();
            } while(!metaDataPage.isEmpty() && nextPageToken != null);

            // Add Plugin Listener
            // TODO: Maintain static listener for every user
            FileQueueHandler.getInstance().listen(drivePlugin);
        } catch (PluginException e) {
            throw new IntegrationException("Failed to integrate data for Google Drive", e);
        } catch (DataReaderException e) {
            throw new IntegrationException("Failed to connect to Google Drive", e);
        }
    }

}