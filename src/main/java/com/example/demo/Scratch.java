package com.example.demo;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Used during live demo portion of event.
 */
public class Scratch {
    public static void main(String[] args) {
        CompositeMeterRegistry compositeMeterRegistry = new CompositeMeterRegistry();
        MeterRegistry registry = new SimpleMeterRegistry();
        compositeMeterRegistry.add(registry);

        AtomicInteger n = new AtomicInteger();
        registry.gauge("my.gauge", Tags.empty(), n, AtomicInteger::longValue);

        Counter counter = compositeMeterRegistry.counter("my.counter");
        counter.increment();

        Timer t = registry.timer("my.timer");
        t.record(Duration.ofMillis(10));

        // function start
        // GC pause took 1s
        t.record(() -> {
            // 10ms
            // I'm calling a web service
            // time spent on the network
            // time spent satisfying the request at the web service
        });
        // function end

        Timer.builder("my.timer")
                .publishPercentileHistogram()
                .sla(Duration.ofMillis(10), Duration.ofMillis(100))
                .register(registry);

        Timer.builder("http.requests")
                .tag("uri", "/persons")
                .tag("method", "GET")
                .tag("app", "peopleapp")
                .tag("response", "200")
                .tag("userid", "123")
                .tag("instance", "abcd1345");
    }
}