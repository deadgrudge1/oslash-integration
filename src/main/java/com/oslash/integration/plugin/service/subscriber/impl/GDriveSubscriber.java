package com.oslash.integration.plugin.service.subscriber.impl;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.oslash.integration.Main;
import com.oslash.integration.plugin.exception.SubscriberException;
import com.oslash.integration.plugin.service.reader.DataReader;
import com.oslash.integration.plugin.service.reader.impl.GDriveDataReader;
import com.oslash.integration.plugin.service.subscriber.Subscriber;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

public class GDriveSubscriber implements Subscriber {
    private static final String APPLICATION_NAME = "Google Drive API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final List<String> SCOPES = Arrays.asList(DriveScopes.DRIVE_METADATA_READONLY, DriveScopes.DRIVE_READONLY);
    private static final String CREDENTIALS_FILE_PATH = "credentials.json";

    private Drive drive;

    @Override
    public boolean connect(String accessToken) throws SubscriberException {
        final NetHttpTransport HTTP_TRANSPORT;
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        } catch (GeneralSecurityException | IOException e) {
            throw new SubscriberException("Failed to initialize connection", e);
        }

        // Load client secrets.
        InputStream in = Main.class.getClassLoader().getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new SubscriberException("Resource not found: " + CREDENTIALS_FILE_PATH, new FileNotFoundException());
        }

        GoogleClientSecrets clientSecrets;
        try {
            clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
        } catch (IOException e) {
            throw new SubscriberException("Failed to load client secrets", e);
        }

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow;
        try {
            flow = new GoogleAuthorizationCodeFlow.Builder(
                    HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                    .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                    .setAccessType("offline")
                    .build();
        } catch (IOException e) {
            throw new SubscriberException("Failed to load token data store", e);
        }

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8081).build();

        Credential credential;
        try {
            credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("amitchaudhari228");
        } catch (IOException e) {
            throw new SubscriberException("Failed to authorize user", e);
        }

        drive = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME).build();

        return true;
    }

    @Override
    public DataReader getDataReader() throws SubscriberException {
        // New Request for every instance of reader (assuming user is authorized once authenticated)
        try {
            connect("");
        } catch (SubscriberException e) {
            throw new SubscriberException(e);
        }

        return new GDriveDataReader(drive);
    }

}