package com.demo.reactive.springrest.item;

import org.springframework.context.ApplicationEvent;

public class ItemCreatedEvent extends ApplicationEvent {

	private static final long serialVersionUID = -6848184287205635123L;

	public ItemCreatedEvent(Item item) {
		super(item);
	}
}
