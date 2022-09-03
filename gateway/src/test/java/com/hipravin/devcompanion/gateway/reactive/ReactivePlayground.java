package com.hipravin.devcompanion.gateway.reactive;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.List;

public class ReactivePlayground {

    @Test
    void fluxRange() throws InterruptedException {

        Flux<Integer> flux = Mono.just(1).repeat().delayElements(Duration.ofSeconds(1));

//        Flux<List<Integer>> flux = Flux.range(5, 5)
//                .delayElements(Duration.ofSeconds(1))
//                .map(i -> i + 1)
//                .filter(i -> i % 2 != 17)
//                .buffer(3);


        flux.publishOn(Schedulers.boundedElastic()).subscribe(l -> {
            System.out.printf("Thread: %s %s%n", Thread.currentThread().getName(), Thread.currentThread().isDaemon());
            System.out.println(l);
        });


        Thread.sleep(10_000);
    }
}
