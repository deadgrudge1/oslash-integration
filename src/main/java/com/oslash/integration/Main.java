package com.oslash.integration;

import com.google.api.services.drive.Drive;
import com.oslash.integration.plugin.GDrivePlugin;
import com.oslash.integration.plugin.Plugin;
import com.oslash.integration.plugin.metadata.MetaData;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@SpringBootApplication
public class Main {

    public static void main(String... args) throws GeneralSecurityException, IOException {
        Plugin<Drive> plugin = new GDrivePlugin();
        List<MetaData> metaDataList = plugin.read();
        for (MetaData metaData : metaDataList) {
            System.out.println(metaData.getName());
        }
    }

}