package com.oslash.integration.executor.service.timer;

public abstract class AbstractTimer {
    private int countDownSeconds;
    private Thread thread;

    protected AbstractTimer(int countDownSeconds) {
        this.countDownSeconds = countDownSeconds;
    }

    public void start() {
        if(thread != null) {
            thread.stop();
        }

        thread = new Thread(new TimerThread());
        thread.start();
    }

    public void stop() {
        thread.stop();
    }

    protected abstract void process();

    private class TimerThread implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    process();

                    Thread.sleep(countDownSeconds * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}