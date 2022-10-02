package com.oslash.integration.executor.service.queue;

import com.oslash.integration.executor.model.event.BatchEvent;
import com.oslash.integration.executor.model.event.BenchMark;
import com.oslash.integration.executor.model.event.FileEvent;
import com.oslash.integration.executor.service.processor.FileBatchProcessor;
import com.oslash.integration.plugin.constant.PluginType;

import java.util.List;
import java.util.UUID;

public class GDriveFileProcessorQueue extends AbstractQueue<FileEvent> implements Queue<FileEvent> {
    private String integrationId;
    private String metaDataPath;
    private int batchesProcessed = 0;

    private BenchMark benchMark;

    public GDriveFileProcessorQueue(String metaDataPath, int batchSize) {
        super(batchSize);

        this.metaDataPath = metaDataPath;
        this.integrationId = UUID.randomUUID().toString().substring(0, 6).toUpperCase();

        this.benchMark = new BenchMark(PluginType.DRIVE, integrationId, batchSize, metaDataPath);
    }

    @Override
    public void processAll(List<FileEvent> fileEventList) {
        if(fileEventList != null && !fileEventList.isEmpty()) {
            int itemCount = 1;
            BatchEvent batchEvent = new BatchEvent(PluginType.DRIVE, metaDataPath, integrationId, String.valueOf(batchesProcessed));

            for(FileEvent fileEvent : fileEventList) {
                batchEvent.addFileEvent(fileEvent.withIntegrationId(integrationId));

                if(itemCount % batchSize == 0) {
                    // Submit batch in Single Thread Executor Queue
                    batchExecutorService.execute(
                            new FileBatchProcessor(batchEvent).withBenchMark(benchMark)
                    );
                    batchesProcessed++;

                    batchEvent = new BatchEvent(PluginType.DRIVE, metaDataPath, integrationId, String.valueOf(batchesProcessed));
                }

                itemCount++;
            }

            if(batchEvent.getFileEventList() != null && !batchEvent.getFileEventList().isEmpty()) {
                // Submit batch in Single Thread Executor Queue
                batchExecutorService.execute(
                        new FileBatchProcessor(batchEvent).withBenchMark(benchMark)
                );
                batchesProcessed++;
            }

        }
    }

    @Override
    public void process() {
        if(!queue.isEmpty()) {
            BatchEvent batchEvent = new BatchEvent(PluginType.DRIVE, metaDataPath, integrationId, String.valueOf(batchesProcessed));

            // Poll items (batch size) from queue
            int index = 0;
            while(index < batchSize && !queue.isEmpty()) {
                FileEvent fileEvent = queue.poll();
                batchEvent.addFileEvent(fileEvent.withIntegrationId(integrationId));

                index++;
            }

            // Submit batch in Single Thread Executor Queue
            batchExecutorService.execute(
                    new FileBatchProcessor(batchEvent).withBenchMark(benchMark)
            );

            batchesProcessed++;
        }
    }
}