package com.oslash.integration.plugin;

import com.oslash.integration.executor.model.event.FileEvent;
import com.oslash.integration.executor.service.queue.Queue;
import com.oslash.integration.plugin.constant.PluginType;
import com.oslash.integration.plugin.exception.ListenerException;
import com.oslash.integration.plugin.exception.PluginException;
import com.oslash.integration.plugin.exception.SubscriberException;
import com.oslash.integration.plugin.service.listener.GDriveFileChangesListener;
import com.oslash.integration.plugin.service.listener.Listener;
import com.oslash.integration.plugin.service.reader.DataReader;
import com.oslash.integration.plugin.service.subscriber.Subscriber;

public class GDrivePlugin implements Plugin {
    private PluginType pluginType = PluginType.DRIVE;
    private Subscriber subscriber;

    public GDrivePlugin(Subscriber subscriber) {
        this.subscriber = subscriber;
    }

    @Override
    public boolean connect(String accessToken) throws PluginException {
        try {
            return subscriber.connect(accessToken);
        } catch (SubscriberException e) {
            throw new PluginException(e);
        }
    }

    @Override
    public DataReader getDataReader() throws PluginException {
        try {
            return subscriber.getDataReader();
        } catch (SubscriberException e) {
            throw new PluginException(e);
        }
    }

    @Override
    public Listener getFileChangesListener(Queue<FileEvent> queue) throws PluginException {
        try {
            return new GDriveFileChangesListener(pluginType, subscriber.getDataReader(), queue);
        } catch (SubscriberException | ListenerException e) {
            throw new PluginException(e);
        }
    }

    @Override
    public PluginType getType() {
        return pluginType;
    }

}