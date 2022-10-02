package com.oslash.integration.executor.service.processor;

import com.oslash.integration.api.dao.MetaDataDao;
import com.oslash.integration.api.dao.impl.MetaDataDaoImpl;
import com.oslash.integration.common.Utility;
import com.oslash.integration.executor.model.event.BatchEvent;
import com.oslash.integration.executor.model.event.BenchMark;
import com.oslash.integration.executor.model.event.FileEvent;
import com.oslash.integration.executor.service.worker.FileWorker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class FileBatchProcessor implements Runnable {
    private MetaDataDao metaDataDao;
    private BenchMark benchMark;

    private BatchEvent batchEvent;

    public FileBatchProcessor(BatchEvent batchEvent) {
        this.batchEvent = batchEvent;
        this.metaDataDao = new MetaDataDaoImpl();
    }

    public FileBatchProcessor withBenchMark(BenchMark benchMark) {
        this.benchMark = benchMark;
        return this;
    }

    @Override
    public void run() {
        // Executor to process multiple events in parallel
        ExecutorService executorService = Executors.newFixedThreadPool(batchEvent.getBatchSize());
        CompletionService<FileEvent> completionService = new ExecutorCompletionService<>(executorService);

        beginBatch();

        int processedEvents = processEvents(completionService);
        boolean success = collectResult(completionService, processedEvents);

        endBatchEvent(success);
    }

    /**
     * PROCESS ALL EVENTS IN BATCH
     * @param completionService
     * @return
     */
    private int processEvents(CompletionService<FileEvent> completionService) {
        // Process all Items -> Submit tasks
        AtomicInteger itemCount = new AtomicInteger();

        batchEvent.getFileEventList().forEach(fileEvent -> {
            completionService.submit(
                    new FileWorker(fileEvent).withBenchMark(benchMark)
            );

            itemCount.getAndIncrement();
        });

        return itemCount.get();
    }

    /**
     * COLLECT RESULT FOR ALL EVENTS IN BATCH
     * @param completionService
     * @param processedEvents
     * @return
     */
    private boolean collectResult(CompletionService<FileEvent> completionService, int processedEvents) {
        // Wait for all task completion and store success values for each task
        List<Boolean> successList = new ArrayList<>();

        try {
            int index = 0;
            boolean error = false;
            while(index < processedEvents && !error) {
                Future<FileEvent> future = completionService.take();
                try {
                    FileEvent fileEvent = future.get();
                    successList.add(fileEvent.isSuccess());

                    index++;
                } catch (Exception e) {
                    e.printStackTrace();
                    error = true;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return Utility.getFinalResultFromBatch(successList);
    }

    /**
     * BEGIN EVENT
     */
    private void beginBatch() {
        batchEvent.start();
    }

    /**
     * END EVENT
     * @param success
     */
    private void endBatchEvent(boolean success) {
        batchEvent.end(success);
        metaDataDao.saveBatchEvent(batchEvent);

        if(benchMark != null) {
            benchMark.completeBatch(batchEvent.getTimeTaken());
            metaDataDao.saveBenchMark(benchMark);
        }
    }

}