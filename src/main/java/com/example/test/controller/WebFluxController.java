package com.example.test.controller;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class WebFluxController {

    @GetMapping("/product/{id}")
    public Mono<String> getProduct(@PathVariable String id) {
        return Mono.just("Product with ID: " + id);
    }

    @GetMapping("/products")
    public Flux<String> getAllProducts() {
        return Flux.fromIterable(List.of("Product A", "Product B", "Product C"))
                   .delayElements(Duration.ofSeconds(1)); // Simulate stream
    }

    @GetMapping(value = "/stream", produces = "text/event-stream")
    public Flux<String> streamData() {
        return Flux.interval(Duration.ofSeconds(1))
                   .map(tick -> "Current time: " + LocalTime.now());
    }
}