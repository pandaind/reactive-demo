package com.demo.reactive.springrest.item;


import java.util.UUID;
import java.util.function.Predicate;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.util.StringUtils;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@DataMongoTest
@Import(ItemService.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ItemServiceTest {

    private final ItemService service;

    private final ItemRepository repository;

    @Autowired
    public ItemServiceTest(ItemService service, ItemRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    /*@Test
    @Order(1)*/
    public void all() throws InterruptedException {
        Flux<Item> saved = this.repository.saveAll(Flux.just(
                new Item(null, "iPhone"),
                new Item(null, "One Plus"),
                new Item(null, "Pixel")
        ));

        //Thread.sleep(10000);

        Flux<Item> composite = this.service.all().thenMany(saved);

        Predicate<Item> match = item -> saved.any(saveItem -> saveItem.getName().equals(item.getName())).block();

        StepVerifier
                .create(composite)
                .expectNextMatches(match)
                .expectNextMatches(match)
                .expectNextMatches(match)
                .verifyComplete();
    }

    @Test
    public void get() {
        String randomText = UUID.randomUUID().toString();

        Mono<Item> savedItem = this.service
                .create(randomText)
                .flatMap(saved -> this.service.get(saved.getId()));

        StepVerifier
                .create(savedItem)
                .expectNextMatches(item -> StringUtils.hasText(item.getId()) && randomText.equalsIgnoreCase(item.getName()))
                .verifyComplete();
    }

    @Test
    public void update() throws Exception {
        Mono<Item> updated = this.service
                .create("iPhone 11")
                .flatMap(i -> this.service.update(i.getId(), "iPhone 11 Pro"));

        StepVerifier
                .create(updated)
                .expectNextMatches(i -> i.getName().equalsIgnoreCase("iPhone 11 Pro"))
                .verifyComplete();
    }

    @Test
    public void delete() {
        String testItem = "iPhone 11";

        Mono<Item> deleted = this.service
                .create(testItem)
                .flatMap(saved -> this.service.delete(saved.getId()));

        StepVerifier
                .create(deleted)
                .expectNextMatches(item -> item.getName().equalsIgnoreCase(testItem))
                .verifyComplete();
    }

    @Test
    public void create() {

        Mono<Item> item = this.service.create("iPhone 11");

        StepVerifier
                .create(item)
                .expectNextMatches(saved -> StringUtils.hasText(saved.getId()))
                .verifyComplete();
    }
}
