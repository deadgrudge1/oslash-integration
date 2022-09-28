package com.oslash.integration.plugin.subscriber;

import com.oslash.integration.plugin.reader.DataReader;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * Subscriber : Authorize Source and get Data Reader Service configured with Source
 *
 * @author amitchaudhari228@gmail.com
 * Sept-2022
 */

public interface Subscriber<Service> {

    void authorize() throws GeneralSecurityException, IOException;

    DataReader getDataReader() throws GeneralSecurityException, IOException;

    void subscribe();

}