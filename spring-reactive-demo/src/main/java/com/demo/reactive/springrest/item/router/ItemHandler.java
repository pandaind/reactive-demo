package com.demo.reactive.springrest.item.router;

import java.net.URI;

import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.demo.reactive.springrest.item.Item;
import com.demo.reactive.springrest.item.ItemService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class ItemHandler {
	
	private final ItemService service;
	
	@Autowired
	public ItemHandler(ItemService service) {
		this.service = service;
	}
	
	
	Mono<ServerResponse> getById(ServerRequest req) {
        return defaultReadResponse(this.service.get(id(req)));
    }
	
	Mono<ServerResponse> all(ServerRequest r) {
	        return defaultReadResponse(this.service.all());
	}
	
	Mono<ServerResponse> update(ServerRequest req) {
        Flux<Item> item = req.bodyToFlux(Item.class)
            .flatMap(i -> this.service.update(id(req), i.getName()));
        return defaultReadResponse(item);
    }
	
	Mono<ServerResponse> create(ServerRequest req) {
        Flux<Item> item = req
            .bodyToFlux(Item.class)
            .flatMap(i -> this.service.create(i.getName()));
        return defaultWriteResponse(item);
    }
	
	Mono<ServerResponse> delete(ServerRequest req) {
		return defaultReadResponse(this.service.delete(id(req)));
	}
	
	
	private static Mono<ServerResponse> defaultWriteResponse(Publisher<Item> items) {
        return Mono
            .from(items)
            .flatMap(i -> ServerResponse
                .created(URI.create("/items/" + i.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .build());
    }
	
	private static Mono<ServerResponse> defaultReadResponse(Publisher<Item> items) {
        return ServerResponse
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(items, Item.class);
    }
	
	private static String id(ServerRequest req) {
        return req.pathVariable("id");
    }

}
