package com.oslash.integration.plugin.model.impl;

import com.oslash.integration.plugin.model.Plugin;
import com.oslash.integration.plugin.processor.Processor;
import com.oslash.integration.plugin.subscriber.Subscriber;

public class GDrivePlugin implements Plugin {
    private Subscriber subscriber;
    private Processor processor;

    public GDrivePlugin(Subscriber subscriber, Processor processor) {
        this.subscriber = subscriber;
        this.processor = processor;
    }

    @Override
    public Subscriber getSubscriber() {
        return subscriber;
    }

    @Override
    public Processor getProcessor() {
        return processor;
    }

}