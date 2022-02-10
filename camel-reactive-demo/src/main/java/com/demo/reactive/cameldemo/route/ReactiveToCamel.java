package com.demo.reactive.cameldemo.route;

import java.time.Duration;

import org.apache.camel.component.reactive.streams.api.CamelReactiveStreamsService;
import org.apache.camel.spring.SpringRouteBuilder;
import org.reactivestreams.Subscriber;
import org.springframework.beans.factory.annotation.Autowired;

import reactor.core.publisher.Flux;

public class ReactiveToCamel extends SpringRouteBuilder {

    @Autowired
    private CamelReactiveStreamsService camel;

    @Override
    public void configure() throws Exception {

        // Get a subscriber from camel
        Subscriber<String> items = camel.streamSubscriber("items", String.class);

        Flux.interval(Duration.ofSeconds(5))
                .map(i -> "Item " + i)
                .subscribe(items);


        from("reactive-streams:items")
                .setBody().simple("received ${body}")
                .to("log:INFO");
    }
}
