package com.oslash.integration.api.dao;

import com.oslash.integration.executor.model.event.BatchEvent;
import com.oslash.integration.executor.model.event.BenchMark;
import com.oslash.integration.executor.model.event.FileEvent;

public interface MetaDataDao {

    void saveFileEvent(FileEvent fileEvent);

    void saveBatchEvent(BatchEvent batchEvent);

    BenchMark loadBenchMark(String baseUrl, String integrationId);

    void saveBenchMark(BenchMark benchMark);

}