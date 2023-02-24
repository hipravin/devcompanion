package com.hipravin.devcompanion.gateway.playground;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.Clock;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneId;

@RestController
@RequestMapping("/api/v1/playground")
public class LongPollingController {
    private final Clock utcMillisClock = Clock.tickMillis(ZoneId.of("UTC"));

    @RequestMapping("/instant")
    public PlaygroundDto instant() {

        OffsetDateTime start = OffsetDateTime.now(utcMillisClock);
        OffsetDateTime end = OffsetDateTime.now(utcMillisClock).plus(Duration.ofMillis(1));

        return new PlaygroundDto(start, end, "Instant result");
    }

    @RequestMapping("/delayed-present-mono")
    public Mono<PlaygroundDto> delayedMono() {
        OffsetDateTime start = OffsetDateTime.now(utcMillisClock);

        return Mono.delay(Duration.ofSeconds(10))
                .then(Mono.defer(() -> Mono.just(new PlaygroundDto(
                        start, OffsetDateTime.now(utcMillisClock), "Delayed with mono"))));
    }

    @RequestMapping("/delayed-empty-mono")
    public Mono<PlaygroundDto> delayedEmptyMono() {
        return Mono.delay(Duration.ofSeconds(10))
                .then(Mono.empty());
    }

}
