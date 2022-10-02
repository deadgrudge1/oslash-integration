package com.oslash.integration.executor.service.worker;

import com.oslash.integration.api.dao.MetaDataDao;
import com.oslash.integration.api.dao.impl.MetaDataDaoImpl;
import com.oslash.integration.common.Utility;
import com.oslash.integration.executor.model.event.BenchMark;
import com.oslash.integration.executor.model.event.FileEvent;
import com.oslash.integration.plugin.service.reader.DataReader;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Callable;

import static com.oslash.integration.executor.constant.ExecutorConstant.FILE_RETRY_COUNT;

public class FileWorker implements Callable<FileEvent> {
    private MetaDataDao metaDataDao;
    private BenchMark benchMark;

    private DataReader dataReader;
    private FileEvent fileEvent;

    public FileWorker(FileEvent fileEvent) {
        this.dataReader = fileEvent.getDataReader();
        this.fileEvent = fileEvent;

        this.metaDataDao = new MetaDataDaoImpl();
    }

    public FileWorker withBenchMark(BenchMark benchMark) {
        this.benchMark = benchMark;

        return this;
    }

    @Override
    public FileEvent call() {
        beginFileEvent();

        boolean isSuccess = processFileEvent(fileEvent);

        endFileEvent(isSuccess, fileEvent.isAlreadyPresent());

        return fileEvent;
    }

    private boolean processFileEvent(FileEvent fileEvent) {
        // Init File
        File file = new File(fileEvent.getAbsoluteFilePath());

        // Check if file already present
        boolean isIdenticalFilePresent = false;
        if(file.exists() && file.length() > 0) {
            isIdenticalFilePresent = Utility.isMD5CheckSumMatch(file, fileEvent.getFileChecksum());
        }

        boolean isSuccess;

        // File not already present
        if(!isIdenticalFilePresent) {
            FileOutputStream fileOutputStream;
            try {
                fileOutputStream = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                System.out.println("File not found : " + file.getAbsolutePath());
                return false;
            }

            // Get File - DOWNLOAD
            isSuccess = downloadFile(fileOutputStream, fileEvent);

            try {
                fileOutputStream.close();
            } catch (IOException e) {
                System.out.println("Unable to close file stream");
            }
        }
        else {
            System.out.println("File already exists (" + fileEvent.getFileId() + ") - SKIPPING");
            fileEvent.setAlreadyPresent(true);
            return true;
        }

        fileEvent.setFileSizeBytesProcessed(file.length());

        return isSuccess;
    }

    private boolean downloadFile(FileOutputStream fileOutputStream, FileEvent fileEvent) {
        boolean downloadSuccess = false;
        int retryCount = FILE_RETRY_COUNT;

        // Get File - Keep retrying if failed ()
        while(!downloadSuccess && retryCount > 0) {
            try {
                dataReader.getFile(fileOutputStream, fileEvent.getMetaData());

                downloadSuccess = true;
            } catch (com.oslash.integration.plugin.exception.DataReaderException e) {
                e.printStackTrace();

                System.out.println("Failed to get file : RETRYING");
                retryCount--;
            }
        }

        return downloadSuccess;
    }

    private void beginFileEvent() {
        fileEvent.start();
    }

    private void endFileEvent(boolean success, boolean isAlreadyPresent) {
        fileEvent.end(success);
        metaDataDao.saveFileEvent(fileEvent);

        if(benchMark != null && !isAlreadyPresent) {
            benchMark.addItem(fileEvent.getFileSizeBytesProcessed(), fileEvent.getTimeTaken());
        }
    }

}