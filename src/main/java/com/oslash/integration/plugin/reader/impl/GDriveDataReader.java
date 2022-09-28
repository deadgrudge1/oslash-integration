package com.oslash.integration.plugin.reader.impl;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.FileList;
import com.oslash.integration.plugin.model.metadata.GDriveMetaData;
import com.oslash.integration.plugin.model.metadata.MetaData;
import com.oslash.integration.plugin.reader.DataReader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.Collectors;

public class GDriveDataReader implements DataReader {
    private Drive drive;

    public GDriveDataReader(Drive driveService) {
        drive = driveService;
    }

    @Override
    public List<MetaData> getAllMetaData() throws IOException {
        FileList result = drive.files().list()
//                .setPageSize(10)
                .setFields("nextPageToken, files(id, name, md5Checksum, kind)")
                .execute();

        return result
                .getFiles()
                .stream()
                .map(GDriveMetaData::new)
                .collect(Collectors.toList());
    }

    @Override
    public OutputStream getFile(MetaData metaData) throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();

        drive.files()
                .export(
                        metaData.getId(),
                        metaData.getFileType().getFileFormat()
                ).executeAndDownloadTo(outputStream);


        return outputStream;
    }
}