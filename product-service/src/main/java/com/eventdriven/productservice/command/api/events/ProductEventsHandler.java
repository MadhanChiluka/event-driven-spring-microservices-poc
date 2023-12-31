package com.eventdriven.productservice.command.api.events;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.eventdriven.productservice.command.api.data.Product;
import com.eventdriven.productservice.command.api.data.ProductRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@AllArgsConstructor
@ProcessingGroup("product")
@Slf4j
public class ProductEventsHandler {

	private ProductRepository productRepository;

	@EventHandler
	public void on(ProductCreatedEvent event) throws Exception {
		Product product = new Product();
		BeanUtils.copyProperties(event, product);
		productRepository.save(product);
//		throw new Exception("Unable to save product in db");
	}
	
	@ExceptionHandler
	public void handle(Exception exception) throws Exception {
		log.error("Exception Occurred ", exception);
		throw exception;
	}
}
