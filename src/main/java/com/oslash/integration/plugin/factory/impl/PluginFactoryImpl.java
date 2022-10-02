package com.oslash.integration.plugin.factory.impl;

import com.oslash.integration.plugin.GDrivePlugin;
import com.oslash.integration.plugin.Plugin;
import com.oslash.integration.plugin.factory.PluginFactory;
import com.oslash.integration.plugin.service.subscriber.impl.GDriveSubscriber;
import org.springframework.stereotype.Service;

@Service
public class PluginFactoryImpl implements PluginFactory {

    @Override
    public Plugin createDrivePlugin() {
        return new GDrivePlugin(new GDriveSubscriber());
    }

}