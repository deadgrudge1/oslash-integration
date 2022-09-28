package com.oslash.integration.plugin.factory.impl;

import com.oslash.integration.plugin.model.impl.GDrivePlugin;
import com.oslash.integration.plugin.model.Plugin;
import com.oslash.integration.plugin.factory.PluginFactory;
import com.oslash.integration.plugin.processor.impl.GDriveProcessor;
import com.oslash.integration.plugin.subscriber.impl.GDriveSubscriber;
import org.springframework.stereotype.Service;

@Service
public class PluginFactoryImpl implements PluginFactory {

    @Override
    public Plugin createDrivePlugin() {
        return new GDrivePlugin(
                new GDriveSubscriber(),
                new GDriveProcessor()
        );
    }

}