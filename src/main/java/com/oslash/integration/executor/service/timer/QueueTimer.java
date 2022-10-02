package com.oslash.integration.executor.service.timer;

import com.oslash.integration.executor.service.queue.Queue;

public class QueueTimer extends AbstractTimer {
    private Queue queue;

    public QueueTimer(Queue queue, int countDownSeconds) {
        super(countDownSeconds);
        this.queue = queue;
    }

    @Override
    protected void process() {
        queue.process();
    }
}