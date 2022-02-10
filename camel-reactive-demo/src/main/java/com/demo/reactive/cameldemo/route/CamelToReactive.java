package com.demo.reactive.cameldemo.route;

import org.apache.camel.Exchange;
import org.apache.camel.component.reactive.streams.api.CamelReactiveStreamsService;
import org.apache.camel.spring.SpringRouteBuilder;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;

import reactor.core.publisher.Flux;

public class CamelToReactive extends SpringRouteBuilder {

    @Autowired
    private CamelReactiveStreamsService camel;

    @Override
    public void configure() throws Exception {
        from("timer:clock")
                .setBody().header(Exchange.TIMER_COUNTER)
                .to("reactive-streams:exchanges");

        // Getting a stream of exchanges
        Publisher<Exchange> exchanges = camel.fromStream("exchanges");

        Flux.from(exchanges).doOnNext(exchange -> System.out.println("Emitting : " + exchange.getExchangeId()))
                .subscribe(exchange -> System.out.println(exchange.getIn().getBody()));
    }
}
