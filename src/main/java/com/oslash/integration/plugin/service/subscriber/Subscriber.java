package com.oslash.integration.plugin.service.subscriber;

import com.oslash.integration.plugin.exception.SubscriberException;
import com.oslash.integration.plugin.service.reader.DataReader;

/**
 * Subscriber : Authorize Source and get Data Reader Service configured with Source
 *
 * @author amitchaudhari228@gmail.com
 * Sept-2022
 */

public interface Subscriber {

    boolean connect(String accessToken) throws SubscriberException;

    DataReader getDataReader() throws SubscriberException;

}