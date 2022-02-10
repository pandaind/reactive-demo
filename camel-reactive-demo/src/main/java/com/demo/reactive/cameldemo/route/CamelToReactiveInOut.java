package com.demo.reactive.cameldemo.route;

import org.apache.camel.Exchange;
import org.apache.camel.component.reactive.streams.api.CamelReactiveStreamsService;
import org.apache.camel.spring.SpringRouteBuilder;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Flux;


public class CamelToReactiveInOut extends SpringRouteBuilder {

    @Autowired
    private CamelReactiveStreamsService camel;

    @Override
    public void configure() throws Exception {

        from("timer:clock")
                .setBody().header(Exchange.TIMER_COUNTER)
                .to("direct:reactive")
                .log("Continue with Camel route... n=${body}");

        camel.process("direct:reactive", Integer.class, items ->
                Flux.from(items)
                        //.subscribeOn(Schedulers.parallel())
                        .map(n -> {
                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException e) {
                            }
                            return n * n;
                        })
        );


       /* // Generate a Id and retrieve user data from reactor
        from("timer:clock?period=5000")
                .setBody().header(Exchange.TIMER_COUNTER)
                .convertBodyTo(Long.class)
                .bean("appendBean", "append")
                .process(new UnwrapStreamProcessor())
                .log("Received ${body}");*/
    }


    @Component("appendBean")
    public class Append{
        public Publisher<String> append(Publisher<Long> counter) {
            return Flux.from(counter)
                    .map(c -> "Counting " + c)
                    .flatMap(this::appendName)
                    .flatMap(this::appendWhat);
        }

        private Publisher<String> appendName(String c) {
            return Flux.just("A is " + c);
        }

        private Publisher<String> appendWhat(String c) {
            return Flux.just("Person " + c);
        }
    }
}
