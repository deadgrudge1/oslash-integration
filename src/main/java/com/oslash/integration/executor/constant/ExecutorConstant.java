package com.oslash.integration.executor.constant;

public class ExecutorConstant {
    // Maximum amount of events in a batch
    public static int DEFAULT_BATCH_LIMIT = 3;

    // Fixed thread count limit while processing a batch
    public static int DEFAULT_BATCH_EXECUTOR_LIMIT = 10;

    // Acts as a throttle
    public static int DEFAULT_QUEUE_LIMIT = 3;

    // Flush Queue if no incoming items and timeout occurs (Not implemented)
    public static int FLUSH_TIME_OUT_SECONDS = 0;

    public static String BASE_DOWNLOAD_URL = "/home/amit/opt";

    public static final int FILE_RETRY_COUNT = 3;
}