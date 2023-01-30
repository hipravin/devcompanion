package com.hipravin.devcompanion.gateway;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

public class ReactivePlayground {
    @Test
    void testMonoEmpty() {

        Mono<String> mono = Mono.empty();
        Mono<String> mono2 = Mono.empty();

        mono.zipWith(mono2).doOnNext(t2 -> {
                    System.out.println("On success: " + t2.getT1() + " / " + t2.getT2());
                })
                .subscribe();


    }

}
