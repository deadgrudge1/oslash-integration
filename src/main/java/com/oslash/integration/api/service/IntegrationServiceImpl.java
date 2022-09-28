package com.oslash.integration.api.service;

import com.oslash.integration.api.exception.IntegrationException;
import com.oslash.integration.plugin.model.Plugin;
import com.oslash.integration.plugin.factory.PluginFactory;
import com.oslash.integration.plugin.model.metadata.MetaData;
import com.oslash.integration.plugin.reader.DataReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Service
public class IntegrationServiceImpl implements IntegrationService {

    @Autowired
    PluginFactory pluginFactory;

    public void integrateDrive() throws IntegrationException {
        Plugin drivePlugin = pluginFactory.createDrivePlugin();

        DataReader dataReader;

        // Initialise DataReader with authorization
        try {
            dataReader = drivePlugin.getSubscriber().getDataReader();
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            throw new IntegrationException();
        }

        // Failed to get files
        List<MetaData> metaDataList = null;
        try {
             metaDataList = dataReader.getAllMetaData();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // No files found
        if(metaDataList == null || metaDataList.isEmpty()) {
            throw new IntegrationException();
        }

        // Add metadata list to processor queue
    }

}