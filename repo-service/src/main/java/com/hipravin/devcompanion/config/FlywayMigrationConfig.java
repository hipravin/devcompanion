package com.hipravin.devcompanion.config;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfo;
import org.flywaydb.core.api.MigrationInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class FlywayMigrationConfig {
    private static final Logger log = LoggerFactory.getLogger(FlywayMigrationConfig.class);

    @Bean
    public FlywayMigrationStrategy flywayMigrationStrategy() {
        return flyway -> {
            log.info("Skip flyway migration on startup.");

            logMigrationInfo(flyway);
        };
    }

    private void logMigrationInfo(Flyway flyway) {
        MigrationInfoService migrationInfoService = flyway.info();
        MigrationInfo[] applied = migrationInfoService.applied();
        MigrationInfo[] pending = migrationInfoService.pending();

        if(applied.length > 0) {
            log.debug("Migrations applied: ");
            Arrays.stream(migrationInfoService.applied()).forEach(mi -> {
                log.debug("MigrationInfo: '{}' '{}' '{}' '{}' '{}'", mi.getVersion(), mi.getScript(),
                        mi.getDescription(), mi.getInstalledOn(), mi.getState());
            });
        }

        if(pending.length > 0) {
            log.info("Migrations pending: ");
            Arrays.stream(migrationInfoService.pending()).forEach(mi -> {
                log.info("MigrationInfo: '{}' '{}' '{}' '{}'", mi.getVersion(), mi.getScript(), mi.getDescription(), mi.getState());
            });
        }
    }
}
