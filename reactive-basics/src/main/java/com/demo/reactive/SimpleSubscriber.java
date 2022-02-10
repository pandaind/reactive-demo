package com.demo.reactive;

import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;

public class SimpleSubscriber<T> extends BaseSubscriber<T> {
    @Override
    protected void hookOnNext(T value) {
        System.out.println("Simple Subscriber : consumed : " + value);
        request(1);
    }

    @Override
    protected void hookOnSubscribe(Subscription subscription) {
        System.out.println("Simple Subscriber : Subscribed");
        request(1);
    }
}
