package com.hipravin.devcompanion.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Profile(InitRunner.INIT_RUNNERS_PROFILE)
@Order(InitRunner.ORDER_LOG_BUILD_INFO)
public class BuildPropertiesLogger implements ApplicationRunner {
    private static final Logger log = LoggerFactory.getLogger(BuildPropertiesLogger.class);

    final BuildProperties buildProperties;

    public BuildPropertiesLogger(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Override
    public void run(ApplicationArguments args) {
        log.info("===== Build info properties: =====");
        log.info("group: {}", buildProperties.getGroup());
        log.info("artifact: {}", buildProperties.getArtifact());
        log.info("version: {}", buildProperties.getVersion());
        log.info("build time: {}", buildProperties.getTime());
        log.info("===================================");
    }
}
