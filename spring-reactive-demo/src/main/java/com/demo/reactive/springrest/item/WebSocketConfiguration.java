package com.demo.reactive.springrest.item;

import java.util.Collections;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
@Configuration
public class WebSocketConfiguration {
	@Bean
	Executor executor() {
		return Executors.newSingleThreadExecutor();
	}

	@Bean
	HandlerMapping handlerMapping(WebSocketHandler wsh) {
		return new SimpleUrlHandlerMapping() {
			{

				setUrlMap(Collections.singletonMap("/ws/items", wsh));
				setOrder(10);
			}
		};
	}

	@Bean
	WebSocketHandlerAdapter webSocketHandlerAdapter() {
		return new WebSocketHandlerAdapter();
	}

	@Bean
	WebSocketHandler webSocketHandler(ObjectMapper objectMapper, ItemCreatedEventPublisher eventPublisher) {

		Flux<ItemCreatedEvent> publish = Flux.create(eventPublisher).share();

		return session -> {

			Flux<WebSocketMessage> messageFlux = publish.map(evt -> {
				try {
					return objectMapper.writeValueAsString(evt.getSource());
				} catch (JsonProcessingException e) {
					throw new RuntimeException(e);
				}
			}).map(str -> {
				log.info("sending " + str);
				return session.textMessage(str);
			});

			return session.send(messageFlux);
		};
	}
}
