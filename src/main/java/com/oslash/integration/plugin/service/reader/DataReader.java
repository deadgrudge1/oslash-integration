package com.oslash.integration.plugin.service.reader;

import com.oslash.integration.plugin.exception.DataReaderException;
import com.oslash.integration.plugin.model.MetaData;
import com.oslash.integration.plugin.model.MetaDataPage;

import java.io.OutputStream;
import java.util.List;

/**
 * Reader : Get MetaData, Files and more from Source
 *
 * @author amitchaudhari228@gmail.com
 * Sept-2022
 */

public interface DataReader {

    List<MetaData> getAllMetaData() throws DataReaderException;

    MetaDataPage getMetaData(String pageToken) throws DataReaderException;

    void getFile(OutputStream targetOutputStream, MetaData metaData) throws DataReaderException;

    List<MetaData> getChanges(String startPageToken) throws DataReaderException;

    String newStartPageToken() throws DataReaderException;

}