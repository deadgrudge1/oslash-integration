package com.oslash.integration.executor.service.queue;

import com.oslash.integration.executor.model.event.FileEvent;

import java.util.List;

public interface Queue<Event> {

    void add(Event event);

    void addAll(List<Event> events);

    void processAll(List<FileEvent> fileEventList);

    void process();

}