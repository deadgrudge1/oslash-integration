package com.oslash.integration.api.dao.impl;

import com.google.gson.Gson;
import com.oslash.integration.api.dao.MetaDataDao;
import com.oslash.integration.executor.model.event.BatchEvent;
import com.oslash.integration.executor.model.event.BenchMark;
import com.oslash.integration.executor.model.event.FileEvent;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class MetaDataDaoImpl implements MetaDataDao {

    @Override
    public void saveFileEvent(FileEvent fileEvent) {
        String batchJson = new Gson().toJson(fileEvent);

        try {
            String integrationId = "TEST";
            if(fileEvent.getIntegrationId() != null) {
                integrationId = fileEvent.getIntegrationId();
            }

            Files.write(Paths.get(fileEvent.getBasePath() + "/" + "MetaData_FILE_" + integrationId + "_" + fileEvent.getFileId() + ".json"), batchJson.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveBatchEvent(BatchEvent batchEvent) {
        String batchJson = new Gson().toJson(batchEvent);

        try {
            String integrationId = "TEST";
            if(batchEvent.getIntegrationId() != null) {
                integrationId = batchEvent.getIntegrationId();
            }

            Files.write(Paths.get(batchEvent.getBasePath() + "/" + "MetaData_BATCH_" + integrationId + "_" + batchEvent.getJobId() + ".json"), batchJson.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public BenchMark loadBenchMark(String baseUrl, String integrationId) {
        String data = null;
        try {
            Path path = Paths.get(baseUrl + "/" + "BenchMark_" + integrationId + ".json");
            Stream<String> lines = Files.lines(path);
            data = lines.collect(Collectors.joining("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(data != null) {
            return new Gson().fromJson(data, BenchMark.class);
        }

        return null;
    }

    @Override
    public void saveBenchMark(BenchMark benchMark) {
        String benchMarkJson = new Gson().toJson(benchMark);

        try {
            String integrationId = "TEST";
            if(benchMark.getIntegrationId() != null) {
                integrationId = benchMark.getIntegrationId();
            }

            Files.write(Paths.get(benchMark.getBasePath() + "/" + "BenchMark_" + integrationId + ".json"), benchMarkJson.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}