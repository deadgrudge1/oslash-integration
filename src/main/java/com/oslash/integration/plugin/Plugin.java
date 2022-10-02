package com.oslash.integration.plugin;

import com.oslash.integration.executor.model.event.FileEvent;
import com.oslash.integration.executor.service.queue.Queue;
import com.oslash.integration.plugin.constant.PluginType;
import com.oslash.integration.plugin.exception.PluginException;
import com.oslash.integration.plugin.service.listener.Listener;
import com.oslash.integration.plugin.service.reader.DataReader;

/**
 * Model : Subscribe to source and Process Data
 *
 * @author amitchaudhari228@gmail.com
 * Sept-2022
 */

public interface Plugin {

    PluginType getType();

    boolean connect(String accessToken) throws PluginException;

    DataReader getDataReader() throws PluginException;

    Listener getFileChangesListener(Queue<FileEvent> queue) throws PluginException;

}