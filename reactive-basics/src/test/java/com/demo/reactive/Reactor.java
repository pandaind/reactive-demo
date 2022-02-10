package com.demo.reactive;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class Reactor {

    @Ignore
    @Test
    public void flux() {
        Flux<String> seq1 = Flux.just("foo", "bar", "foobar");

        List<String> iterable = Arrays.asList("foo", "bar", "foobar");
        Flux<String> seq2 = Flux.fromIterable(iterable);

//        seq1.subscribe(System.out::println).dispose();

//        seq2.subscribe(System.out::println, System.err::println,() -> System.out.println("Done")).dispose();

        Flux<Integer> numbersFromOneToFive = Flux.range(1, 5)
                .map(i -> {
                    if (i == 4)
                        throw new RuntimeException("Got to 4");
                    return i;
                });

       // numbersFromOneToFive.subscribe(i -> System.out.println(i), error -> System.err.println("Error: " + error));

        /*Flux<Integer> ints = Flux.range(1, 4);
        ints.subscribe(i -> System.out.println(i),
                error -> System.err.println("Error " + error),
                () -> System.out.println("Done"),
                sub -> sub.request(10)).dispose();*/

       /*SimpleSubscriber<String> ss = new SimpleSubscriber<>();
       seq2.subscribe(ss);
       ss.dispose();*/

       /*Flux.just(1, 2, 0, 3)
                .map(i -> "100 / " + i + " = " + (100 / i))
                //.onErrorReturn("Divide By Zero")
                .onErrorResume(e -> Flux.error(e))
                .retry(3)
                .subscribe(System.out::println);*/

       assertTrue(true);
    }

    @Ignore
    @Test
    public void mono(){
        Mono<String> noData = Mono.empty();

        Mono<String> data = Mono.just("foo");

        noData.subscribe(System.out::print);
        data.subscribe(System.out::print);

        assertTrue(true);
    }
}
