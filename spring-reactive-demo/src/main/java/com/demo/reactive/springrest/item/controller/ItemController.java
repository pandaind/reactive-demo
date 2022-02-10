package com.demo.reactive.springrest.item.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.reactive.springrest.item.Item;
import com.demo.reactive.springrest.item.ItemService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "items", produces = MediaType.APPLICATION_JSON_VALUE)
public class ItemController {

    private final ItemService service;

    @Autowired
    public ItemController(ItemService service) {
        this.service = service;
    }

    @GetMapping
    public Flux<Item> getAll() {
        return this.service.all();
    }

    @GetMapping("/{id}")
    public Mono<Item> getById(@PathVariable("id") String id) {
        return this.service.get(id);
    }

    @PostMapping
    public Mono<ResponseEntity<Item>> create(@RequestBody Mono<Item> item) {
        return item
                .flatMap(i -> this.service.create(i.getName())
                .map(savedItem -> ResponseEntity.created(URI.create("/items/"+savedItem.getId()))
                        .contentType(MediaType.APPLICATION_JSON).body(savedItem)));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Item>> update(@PathVariable String id, @RequestBody Mono<Item> item) {
        return item
                .flatMap(i -> this.service.update(id, i.getName())
                .map(updateItem -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON).body(updateItem)));
    }

    @DeleteMapping("/{id}")
    public Mono<Item> delete(@PathVariable String id) {
        return this.service.delete(id);
    }
}
