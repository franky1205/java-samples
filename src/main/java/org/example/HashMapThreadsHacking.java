package org.example;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class HashMapThreadsHacking {

    public static void main(String[] args) throws InterruptedException {
        HashMapThreadsHacking hacking = new HashMapThreadsHacking();
        hacking.start();
        System.out.println("Hacking Completed with size of Map: " + hacking.nonSafeMap.size());
        hacking.jobExecutor.shutdown();
    }

    private final Map<Integer, Integer> nonSafeMap = new HashMap<>();

    private final ExecutorService jobExecutor = Executors.newFixedThreadPool(10);

    private final AtomicInteger counter = new AtomicInteger();

    public void start() throws InterruptedException {
        final CountDownLatch countDownLatch = new CountDownLatch(10);
        IntStream.range(0, 10)
                .forEach(index ->
                    jobExecutor.submit(() -> {
                        while (counter.get() < 100000000) {
                            int nextCount = counter.incrementAndGet();
                            nonSafeMap.put(nextCount, nextCount);
                        }
                        countDownLatch.countDown();
                    }));

        boolean terminated = countDownLatch.await(10, TimeUnit.MINUTES);
        if (!terminated) {
            throw new RuntimeException("Not complete successfully within 10 minutes.");
        }
    }
}
