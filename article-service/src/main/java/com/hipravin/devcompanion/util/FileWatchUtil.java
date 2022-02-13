package com.hipravin.devcompanion.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.*;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public abstract class FileWatchUtil {
    private static final Logger log = LoggerFactory.getLogger(FileWatchUtil.class);

    private FileWatchUtil() {
    }

    /**
     * When any sub-path (either file or directory) under <code>dir</code> is modified then
     * <code>onUpdateConsumer</code> is called passing set of modified entries without recursive traversal.
     * This means that if file 'f1.txt' is modified at location 'dir/subpath1/f1.txt' then set will contain only 'subpath1'
     * but not 'subpath1/f1.txt'.
     *
     * @return Returns a runnable that needs to be submitted for execution.
     */
    public static Runnable watchForUpdatesRunnable(Path dir, Consumer<Set<Path>> onUpdateConsumer) {
        log.info("Start watching updates on dir: {}", dir.toString());
        final WatchService watchService = startWatchService(dir);

        return () -> {
            boolean valid = true;
            while (valid && !Thread.currentThread().isInterrupted()) {
                try {
                    WatchKey key = watchService.take();

                    Set<Path> relativePaths = extractModifiedRelativePaths(key);
                    log.info("Updated paths (relative): {}", relativePaths);

                    onUpdateConsumer.accept(relativePaths);

                    valid = key.reset();
                    if (!valid) {
                        log.warn("Stop watching dir '{}' due to key.reset() returned false", dir);
                    }
                } catch (InterruptedException e) {
                    log.info("FileWatch has been terminated due to interrupt at dir '{}'", dir);
                    Thread.currentThread().interrupt();
                }
            }
        };
    }

    private static WatchService startWatchService(Path dir) {
        try {
            WatchService watchService
                    = FileSystems.getDefault().newWatchService();

            dir.register(
                    watchService,
                    StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_DELETE,
                    StandardWatchEventKinds.ENTRY_MODIFY);

            return watchService;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new UncheckedIOException(e);
        }
    }

    private static Set<Path> extractModifiedRelativePaths(WatchKey key ) {
        return key.pollEvents()
                .stream().filter(e -> e.kind() != StandardWatchEventKinds.OVERFLOW)
                .map(e -> ((WatchEvent<Path>) e).context())
                .collect(Collectors.toSet());
    }

}
