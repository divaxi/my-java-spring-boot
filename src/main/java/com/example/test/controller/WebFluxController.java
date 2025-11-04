package com.example.test.controller;

import java.time.Duration;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.test.dto.MonitoringDto;

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

@GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
public Flux<ServerSentEvent<MonitoringDto>> stream() {
    return Flux.interval(Duration.ofSeconds(1))
        .map(seq -> {
            MonitoringDto data = new MonitoringDto(Math.round(Math.random() * 1000) / 10.0, Math.round(Math.random() * 1000) / 10.0);
            return ServerSentEvent.<MonitoringDto>builder()
                    .id(String.valueOf(seq))
                    .event("monitoringEvent")
                    .data(data)
                    .build();
        });
}

}