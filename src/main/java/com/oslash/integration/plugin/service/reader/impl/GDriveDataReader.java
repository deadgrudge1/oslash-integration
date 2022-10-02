package com.oslash.integration.plugin.service.reader.impl;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.Change;
import com.google.api.services.drive.model.ChangeList;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.oslash.integration.plugin.exception.DataReaderException;
import com.oslash.integration.plugin.model.GDriveMetaData;
import com.oslash.integration.plugin.model.MetaData;
import com.oslash.integration.plugin.model.MetaDataPage;
import com.oslash.integration.plugin.service.reader.DataReader;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GDriveDataReader implements DataReader {
    private Drive drive;

    private static final String DRIVE_MIME_TYPES = "" +
            "application/vnd.google-apps.spreadsheet," +
            "application/vnd.google-apps.document," +
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet," +
            "application/vnd.ms-excel," +
            "text/xml," +
            "text/csv," +
            "text/plain," +
            "application/msword," +
            "application/pdf";

    public GDriveDataReader(Drive driveService) {
        drive = driveService;
    }

    @Override
    public List<MetaData> getAllMetaData() throws DataReaderException {
        Drive.Files.List list;

        // Fields
        try {
            list = drive.files().list().setFields("*");
        } catch (IOException e) {
            throw new DataReaderException(e);
        }

        // File Types
        String qParameters = getMimeTypesQueryParameter();
        if(qParameters != null) {
            list.setQ(qParameters);
        }

        // Execute
        FileList result;
        try {
            result = list.execute();
        } catch (IOException e) {
            throw new DataReaderException(e);
        }

        return result
                .getFiles()
                .stream()
                .map(GDriveMetaData::new)
                .collect(Collectors.toList());
    }

    @Override
    public MetaDataPage getMetaData(String pageToken) throws DataReaderException {
        Drive.Files.List list;

        // Fields
        try {
            list = drive.files().list().setFields("*");
        } catch (IOException e) {
            throw new DataReaderException(e);
        }

        // File Types
        String qParameters = getMimeTypesQueryParameter();
        if(qParameters != null) {
            list.setQ(qParameters);
        }

        // Page Token
        if(pageToken != null) {
            list.setPageToken(pageToken);
        }

        // Execute
        FileList result;
        try {
            result = list.execute();
        } catch (IOException e) {
            throw new DataReaderException(e);
        }

        List<MetaData> metaDataList = result
                .getFiles()
                .stream()
                .map(GDriveMetaData::new)
                .collect(Collectors.toList());

        return new MetaDataPage(metaDataList, result.getNextPageToken());
    }

    @Override
    public void getFile(OutputStream targetOutputStream, MetaData metaData) throws DataReaderException {
        // GZip Compression is used during data transfer (as per docs)

        if(metaData.getMimeType() != null && metaData.getMimeType().contains("application/vnd.google")) {
            // Convert Google Docs to PDF files - Set MetaData accordingly
            metaData = new GDriveMetaData(
                    metaData.getId(),
                    metaData.getFileName(),
                    metaData.getName(),
                    "pdf",
                    metaData.getCheckSum(),
                    "application/pdf",
                    metaData.getSize()
            );

            // Write file to OutputStream
            try {
                drive.files()
                        .export(metaData.getId(), metaData.getMimeType())
                        .executeMediaAndDownloadTo(targetOutputStream);
            } catch (IOException e) {
                throw new DataReaderException(e);
            }
        }
        else {
            // Write file to OutputStream
            try {
                drive.files()
                        .get(metaData.getId())
                        .executeMediaAndDownloadTo(targetOutputStream);
            } catch (IOException e) {
                throw new DataReaderException(e);
            }
        }

    }

    @Override
    public String newStartPageToken() throws DataReaderException {
        try {
            return drive.changes().getStartPageToken().execute().getStartPageToken();
        } catch (IOException e) {
            throw new DataReaderException(e);
        }
    }

    @Override
    public List<MetaData> getChanges(String startPageToken) throws DataReaderException {
        Drive.Changes.List changesList;

        // Fields
        try {
            changesList = drive.changes().list(startPageToken).setFields("");
        } catch (IOException e) {
            throw new DataReaderException(e);
        }

        // Execute
        ChangeList result;
        try {
            result = changesList.setFields("*").execute();
        } catch (IOException e) {
            throw new DataReaderException(e);
        }

        // Changed file id list
        List<String> idList = result
                .getChanges()
                .stream()
                .map(Change::getFileId)
                .collect(Collectors.toList());

        System.out.println("GET CHANGES - " + idList);

        List<MetaData> changedFilesMetaData = new ArrayList<>();

        // New File Changes found
        if(!idList.isEmpty()) {
            for(String changedFileId : idList) {
                File file = null;

                // Get MetaData for changed file
                try {
                    file = drive.files().get(changedFileId).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                    // TODO: LOG (File is probably deleted - IGNORE)
                }

                System.out.println("File Extension : " + file);

                if(file!= null && DRIVE_MIME_TYPES.toLowerCase().contains(file.getMimeType().toLowerCase())) {
                    changedFilesMetaData.add(new GDriveMetaData(file));
                }
            }
        }

        return changedFilesMetaData;
    }

    /**
     * Build Drive Service query parameter with configured Mime Types
     */
    private String getMimeTypesQueryParameter() {
        String[] mimeTypes = DRIVE_MIME_TYPES.split(",");
        if(mimeTypes.length > 0) {
            List<String> extensionTypes = new ArrayList<>();

            for(String mimeType : mimeTypes) {
                extensionTypes.add("mimeType=" + "'" + mimeType + "'");
            }

            return String.join(" or ", extensionTypes);
        }

        return null;
    }
}