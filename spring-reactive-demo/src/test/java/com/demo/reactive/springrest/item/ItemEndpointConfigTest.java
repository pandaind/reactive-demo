package com.demo.reactive.springrest.item;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.demo.reactive.springrest.item.router.ItemEndpointConfig;
import com.demo.reactive.springrest.item.router.ItemHandler;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@WebFluxTest
@ContextConfiguration(classes = {ItemEndpointConfig.class, ItemHandler.class, ItemService.class})
public class ItemEndpointConfigTest {
	    
	private WebTestClient webClient;

    @MockBean
    private ItemService service;

    @Autowired
    private ApplicationContext contex;
    
    @BeforeEach
    public void setupApi() {
    	this.webClient = WebTestClient.bindToApplicationContext(contex).build();
    }

    @Test
    public void getAll() {
        Mockito
                .when(this.service.all())
                .thenReturn(Flux.just(new Item("1", "iPhone"), new Item("2", "One Plus")));

        this.webClient
                .get()
                .uri("/items")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.[0].id").isEqualTo("1")
                .jsonPath("$.[0].name").isEqualTo("iPhone")
                .jsonPath("$.[1].id").isEqualTo("2")
                .jsonPath("$.[1].name").isEqualTo("One Plus");

    }

    @Test
    public void create() {
        Item data = new Item("123", UUID.randomUUID().toString());

        Mockito
                .when(this.service.create(Mockito.anyString()))
                .thenReturn(Mono.just(data));

        this.webClient
                .post()
                .uri("/items")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(data), Item.class)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON);
    }

    @Test
    public void delete() {
        Item data = new Item("123", UUID.randomUUID().toString());

        Mockito
                .when(this.service.delete(Mockito.anyString()))
                .thenReturn(Mono.just(data));

        this.webClient
                .delete()
                .uri("/items/" + data.getId())
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void update() {
        Item data = new Item("123", UUID.randomUUID().toString());

        Mockito
                .when(this.service.update(Mockito.anyString() ,Mockito.anyString()))
                .thenReturn(Mono.just(data));

        this.webClient
                .put()
                .uri("/items/" + data.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(data), Item.class)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void getById() {
        Item data = new Item("123", UUID.randomUUID().toString());

        Mockito
                .when(this.service.get(Mockito.anyString()))
                .thenReturn(Mono.just(data));

        this.webClient
                .get()
                .uri("/items/" + data.getId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id").isEqualTo(data.getId())
                .jsonPath("$.name").isEqualTo(data.getName());
    }
}
