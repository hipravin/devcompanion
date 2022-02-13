package com.hipravin.devcompanion.util;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.*;

class FileWatchUtilTest {
    @Test
    void testWatchDir() throws IOException, InterruptedException {
        Path tmpDir = Paths.get(
                Files.createTempDirectory("devcompanionTemp").toFile().getAbsolutePath());


        Path tmpFile = tmpDir.resolve("sampleToBeUpdated.txt");

        AtomicLong updateCounter = new AtomicLong(0);

        //start watching temp dir
        Runnable watchRunnable = FileWatchUtil
                .watchForUpdatesRunnable(tmpDir, paths -> updateCounter.addAndGet(paths.size()));

        Thread watchThread = new Thread(watchRunnable);
        watchThread.start();
        assertFalse(updateCounter.get() > 0);

        //then write file
        Files.writeString(tmpFile, UUID.randomUUID() + " " + LocalDateTime.now());

        Thread.sleep(100);
        watchThread.interrupt();
        watchThread.join();

        //expect file change has been captured
        assertTrue(updateCounter.get() > 0);
    }
}