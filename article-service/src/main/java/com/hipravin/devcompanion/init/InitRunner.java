package com.hipravin.devcompanion.init;

public interface InitRunner {
    /**
     * By default, init runners are enabled. In order to disable need to explicitly enable mentioned profile.
     */
    String INIT_RUNNERS_NOT_DISABLED = "!noInitRunners";

    int ORDER_LOG_BUILD_INFO = 0;
    int ORDER_FILL_INMEMORY = 10;
}
