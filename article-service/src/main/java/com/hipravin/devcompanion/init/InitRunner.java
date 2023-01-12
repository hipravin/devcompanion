package com.hipravin.devcompanion.init;

public final class InitRunner {
    /**
     * By default, init runners are enabled. In order to disable need to explicitly enable mentioned profile.
     */
    public static final String INIT_RUNNERS_NOT_DISABLED = "!noInitRunners";

    public static final int ORDER_LOG_BUILD_INFO = 0;
    public static final int ORDER_FILL_INMEMORY = 10;
}
