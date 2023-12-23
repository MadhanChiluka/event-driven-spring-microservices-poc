package com.eventdriven.productservice.command.api.events;

import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.eventdriven.productservice.command.api.data.Product;
import com.eventdriven.productservice.command.api.data.ProductRepository;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class ProductEventsHandler {

	private ProductRepository productRepository;

	@EventHandler
	public void on(ProductCreatedEvent event) {
		Product product = new Product();
		BeanUtils.copyProperties(event, product);
		productRepository.save(product);
	}
}
