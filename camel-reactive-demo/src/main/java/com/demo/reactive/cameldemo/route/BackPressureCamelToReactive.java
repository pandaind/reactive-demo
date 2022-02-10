package com.demo.reactive.cameldemo.route;

import org.apache.camel.Exchange;
import org.apache.camel.component.reactive.streams.api.CamelReactiveStreamsService;
import org.apache.camel.impl.ThrottlingInflightRoutePolicy;
import org.apache.camel.spring.SpringRouteBuilder;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Flux;


@Component
public class BackPressureCamelToReactive extends SpringRouteBuilder {

    @Autowired
    private CamelReactiveStreamsService camel;

    int c = 0;

    @Override
    public void configure() throws Exception {
        ThrottlingInflightRoutePolicy policy = new ThrottlingInflightRoutePolicy();
        policy.setMaxInflightExchanges(5);

        from("timer:clock")
                .setBody().header(Exchange.TIMER_COUNTER)
                .inOnly("seda:queue");

        from("seda:queue").id("queue")
                .routePolicy(policy)
                .to("reactive-streams:flow");

        //?backpressureStrategy=BUFFER")

        Publisher<Long> flow = camel.fromStream("flow", Long.class);

        Flux.from(flow)
                .subscribe(s -> {
                    c+= 1;
                    if(c <= 5) {
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("Done : " + s);
                });
    }
}
