package com.demo.reactive.springrest.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ItemService {
	
	private final ApplicationEventPublisher publisher;
	
	private final ItemRepository repository;
	
	@Autowired
	public ItemService(ApplicationEventPublisher publisher, ItemRepository repository) {
		this.publisher = publisher;
		this.repository = repository;
	}
	
	public Flux<Item> all(){
		return this.repository.findAll();
	}
	
	public Mono<Item> get(String id){
		return this.repository.findById(id);
	}
	
	public Mono<Item> update(String id, String name){
		return this.repository.findById(id)
				.map(i -> new Item(i.getId(), name))
				.flatMap(this.repository::save);
	}
	
	public Mono<Item> delete(String id){
		return this.repository
				.findById(id)
				.flatMap(i -> this.repository.delete(i)
						.thenReturn(i));
	}
	
	public Mono<Item> create(String name){
		return this.repository
				.save(new Item(null,name))
				.doOnSuccess(i -> this.publisher.publishEvent(new ItemCreatedEvent(i)));
	}
}
