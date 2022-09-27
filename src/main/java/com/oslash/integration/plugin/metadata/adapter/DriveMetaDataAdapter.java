package com.oslash.integration.plugin.metadata.adapter;

import com.google.api.services.drive.model.File;
import com.oslash.integration.plugin.metadata.FileMetaData;
import com.oslash.integration.plugin.metadata.MetaData;

public class DriveMetaDataAdapter implements MetadataAdapter {
    private File file;

    public DriveMetaDataAdapter(File file) {
        this.file = file;
    }

    /**
     * Read required metadata from drive files
     */
    @Override
    public MetaData getMetaData() {
        return new FileMetaData(
                file.getId(),
                file.getName(),
                file.getMd5Checksum()
        );
    }
}