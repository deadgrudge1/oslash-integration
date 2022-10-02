package com.oslash.integration.executor.singleton;

import com.oslash.integration.executor.constant.ExecutorConstant;
import com.oslash.integration.executor.model.event.FileEvent;
import com.oslash.integration.executor.service.queue.GDriveFileProcessorQueue;
import com.oslash.integration.executor.service.queue.Queue;
import com.oslash.integration.executor.service.timer.AbstractTimer;
import com.oslash.integration.executor.service.timer.TimedPublisher;
import com.oslash.integration.executor.service.timer.QueueTimer;
import com.oslash.integration.plugin.Plugin;
import com.oslash.integration.plugin.exception.PluginException;
import com.oslash.integration.plugin.service.listener.Listener;

public class FileQueueHandler {
    private static FileQueueHandler fileQueueHandler;
    private static final Object lock = new Object();

    private Queue<FileEvent> driveQueue;
    private AbstractTimer driveListenerTimer;

    private FileQueueHandler() {
        driveQueue = new GDriveFileProcessorQueue(ExecutorConstant.BASE_DOWNLOAD_URL, ExecutorConstant.DEFAULT_BATCH_LIMIT);

        AbstractTimer driveQueueTimer = new QueueTimer(driveQueue, 5);
        driveQueueTimer.start();
    }

    public static FileQueueHandler getInstance() {
        // Double Check Locking Mechanism
        if(fileQueueHandler == null) {
            synchronized (lock) {
                if(fileQueueHandler == null) {
                    fileQueueHandler = new FileQueueHandler();
                }
            }
        }

        return fileQueueHandler;
    }

    public static FileQueueHandler resetInstance() {
        fileQueueHandler = null;
        return getInstance();
    }

    public Queue<FileEvent> driveFileQueue() {
        if(driveQueue == null) {
            driveQueue = new GDriveFileProcessorQueue(ExecutorConstant.BASE_DOWNLOAD_URL, ExecutorConstant.DEFAULT_BATCH_LIMIT);

            AbstractTimer driveQueueTimer = new QueueTimer(driveQueue, 5);
            driveQueueTimer.start();
        }

        return driveQueue;
    }

    public void listen(Plugin plugin) {
        if(driveListenerTimer != null) {
            driveListenerTimer.stop();
        }

        try {
            Listener driveListener = plugin.getFileChangesListener(driveFileQueue());

            driveListenerTimer = new TimedPublisher(driveListener, 5);
            driveListenerTimer.start();
        } catch (PluginException e) {
            e.printStackTrace();
        }

    }

}