package com.demo.reactive.springrest.item;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import reactor.core.publisher.FluxSink;

@Component
public class ItemCreatedEventPublisher implements ApplicationListener<ItemCreatedEvent>, 
Consumer<FluxSink<ItemCreatedEvent>> {

	private final Executor executor;
	
	private final BlockingQueue<ItemCreatedEvent> queue =
	        new LinkedBlockingQueue<>();
	
	@Autowired
	public ItemCreatedEventPublisher(Executor executor) {
		this.executor = executor;
	}
	
	@Override
	public void accept(FluxSink<ItemCreatedEvent> sink) {
		this.executor.execute(() -> {
            while (true)
                try {
                	ItemCreatedEvent event = queue.take(); 
                    sink.next(event); 
                }
                catch (InterruptedException e) {
                    ReflectionUtils.rethrowRuntimeException(e);
                }
        });
	}

	@Override
	public void onApplicationEvent(ItemCreatedEvent event) {
		this.queue.offer(event);
	}

}
