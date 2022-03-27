package org.example.messaging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Frankie Chao
 */
@Slf4j
@Component
public class MessageSampler {

    @Autowired
    private SampleGateway sampleGateway;

    private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(3);

    @PostConstruct
    public void init() {
        log.info("Start publish scheduled messages...");
        final AtomicInteger counter = new AtomicInteger(0);
        executorService.scheduleAtFixedRate(
                () -> sampleGateway.perform("abc,def,ghi," + counter.incrementAndGet()),
                5, 10, TimeUnit.SECONDS);
    }
}
