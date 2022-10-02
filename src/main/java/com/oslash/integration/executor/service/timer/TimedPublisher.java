package com.oslash.integration.executor.service.timer;

import com.oslash.integration.plugin.exception.ListenerException;
import com.oslash.integration.plugin.service.listener.Listener;

public class TimedPublisher extends AbstractTimer {
    private Listener listener;

    public TimedPublisher(Listener listener, int countDownSeconds) {
        super(countDownSeconds);
        this.listener = listener;
    }

    @Override
    protected void process() {
        try {
            listener.onChange();
        } catch (ListenerException e) {
            e.printStackTrace();
        }
    }
}
