package com.oslash.integration.plugin.reader;

import com.oslash.integration.plugin.model.metadata.MetaData;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * Reader : Get MetaData and their Files from Source
 *
 * @author amitchaudhari228@gmail.com
 * Sept-2022
 */

public interface DataReader {

    List<MetaData> getAllMetaData() throws IOException;

    OutputStream getFile(MetaData metaData) throws IOException;

}