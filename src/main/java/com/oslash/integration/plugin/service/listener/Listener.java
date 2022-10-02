package com.oslash.integration.plugin.service.listener;

import com.oslash.integration.plugin.exception.ListenerException;

/**
 * Listener : Authorize Source and get Data Reader Service configured with Source
 *
 * @author amitchaudhari228@gmail.com
 * Sept-2022
 */

public interface Listener {

    void onChange() throws ListenerException;

}