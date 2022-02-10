package com.demo.reactive.cameldemo.route;

import java.time.Duration;

import org.apache.camel.component.reactive.streams.api.CamelReactiveStreamsService;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import reactor.core.publisher.Flux;

public class ReactiveToCamelInOut extends SpringRouteBuilder {

    @Autowired
    private CamelReactiveStreamsService camel;

    @Override
    public void configure() throws Exception {
        Flux.interval(Duration.ofSeconds(500000))
                .map(i -> i + 1) // start from 1
                .flatMap(camel.toStream("sqr", Double.class)) // call route and continue
                .map(d -> "sqr = " + d)
                .doOnNext(System.out::println)
                .subscribe();

        from("reactive-streams:sqr")
                .setBody().body(Double.class, i -> i * i);
    }
}
