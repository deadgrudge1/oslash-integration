package com.oslash.integration.plugin.model;

import com.oslash.integration.plugin.processor.Processor;
import com.oslash.integration.plugin.subscriber.Subscriber;

/**
 * Model : Subscribe to source and Process Data
 *
 * @author amitchaudhari228@gmail.com
 * Sept-2022
 */

public interface Plugin {

    Subscriber getSubscriber();

    Processor getProcessor();

}