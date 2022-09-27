package com.oslash.integration.plugin;

import com.oslash.integration.plugin.metadata.MetaData;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

public interface Plugin<Connector> {

    Connector authorize() throws GeneralSecurityException, IOException;

    List<MetaData> read() throws GeneralSecurityException, IOException;

    void listen();

}