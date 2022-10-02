package com.oslash.integration.executor.service.queue;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class AbstractQueue<Event> {
    protected BlockingQueue<Event> queue;
    protected ExecutorService batchExecutorService;
    protected int batchSize;

    public AbstractQueue(int batchSize) {
        this.batchSize = batchSize;
        this.queue = new LinkedBlockingQueue<>(batchSize);
        this.batchExecutorService = Executors.newSingleThreadExecutor();
    }

    protected void reset() {
        this.queue = new LinkedBlockingQueue<>(batchSize);
        this.batchExecutorService = Executors.newSingleThreadExecutor();
    }

    public void add(Event event) {
        if(queue.size() == batchSize) {
            process();
        }

        queue.offer(event);
    }

    public void addAll(List<Event> eventList) {
        if(eventList != null) {
            for (Event event : eventList) {
                this.add(event);
            }
        }
    }

    protected abstract void process();

}
