package org.example;

import lombok.extern.slf4j.Slf4j;
import org.example.messaging.SampleGateway;
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
public class MessageSampler {

    @Autowired
    private SampleGateway sampleGateway;

    @Autowired
    private NodeManagerHandler nodeManagerHandler;

    private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(3);

    @PostConstruct
    public void init() {
        this.samplingIntMessage();
        this.samplingWebSocketMessage();
    }

    private void samplingIntMessage() {
        final AtomicInteger counter = new AtomicInteger(0);
        executorService.scheduleAtFixedRate(
                () -> sampleGateway.perform("abc,def,ghi," + counter.incrementAndGet(), "ABC"),
                5, 10, TimeUnit.MINUTES);
    }

    private void samplingWebSocketMessage() {
        final AtomicInteger counter = new AtomicInteger(0);
        executorService.scheduleAtFixedRate(
                () -> {
                    log.info("Start to send broadcasting message to WebSocket clients");
                    nodeManagerHandler.sendBroadcastMessage("Hello Guys -> " + counter.incrementAndGet());
                },
                5, 10, TimeUnit.SECONDS);
    }
}
