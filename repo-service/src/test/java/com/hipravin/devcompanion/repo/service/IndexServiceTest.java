package com.hipravin.devcompanion.repo.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles({"test"})
class IndexServiceTest {

    @Autowired
    IndexService indexService;

    CountDownLatch indexStart = new CountDownLatch(2);

    @Test
    void testConcurrent() {
        AtomicInteger exceptionCount = new AtomicInteger(0);

        Runnable indexAsync = () -> {
            try {
                indexStart.countDown();
                indexStart.await();

                indexService.index();
            } catch(ConcurrentIndexOperationException e) {
                exceptionCount.incrementAndGet();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };

        CompletableFuture<Void> index1 = CompletableFuture.runAsync(indexAsync);
        CompletableFuture<Void> index2 = CompletableFuture.runAsync(indexAsync);

        CompletableFuture.allOf(index1, index2).join();

        assertEquals(1, exceptionCount.get());
    }
}