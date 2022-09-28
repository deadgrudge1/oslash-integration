package com.oslash.integration.plugin.processor;

import com.oslash.integration.plugin.model.metadata.MetaData;

/**
 * Processor : Process file event
 * Save MetaData and Download File
 *
 * @author amitchaudhari228@gmail.com
 * Sept-2022
 */

public interface Processor {

    void processFile(MetaData metaData);

}
