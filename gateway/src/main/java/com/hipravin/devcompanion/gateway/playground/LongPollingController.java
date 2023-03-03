package com.hipravin.devcompanion.gateway.playground;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Clock;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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

    @RequestMapping("/flux-instant")
    public Flux<PlaygroundDto> soHowFluxIsPresented(@RequestParam(name = "count", defaultValue = "10") long count) {
        OffsetDateTime start = OffsetDateTime.now(utcMillisClock);

        Stream<PlaygroundDto> dtoStream = IntStream.range(0, 10)
                .mapToObj(i -> new PlaygroundDto(start, OffsetDateTime.now(utcMillisClock), "Message #%d".formatted(i)))
                .limit(count);
        return Flux.fromStream(dtoStream).log();
    }

    //with default media type (application/json) response will be sent only at the end of processing
    //with SSE every element will be delivered once ready (every second)
    @RequestMapping(value = "/flux-delayed", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<PlaygroundDto> soHowDelayedFluxIsPresented(@RequestParam(name = "count", defaultValue = "10") long count) {
        OffsetDateTime start = OffsetDateTime.now(utcMillisClock);

        Stream<PlaygroundDto> dtoStream = IntStream.range(0, 10)
                .mapToObj(i -> new PlaygroundDto(start, OffsetDateTime.now(utcMillisClock), "Message #%d".formatted(i)))
                .limit(count);
        return Flux.fromStream(dtoStream)
                .delayElements(Duration.ofSeconds(1)).log();
    }


}
