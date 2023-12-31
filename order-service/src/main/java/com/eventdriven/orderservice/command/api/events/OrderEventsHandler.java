package com.eventdriven.orderservice.command.api.events;

import java.util.Objects;
import java.util.Optional;

import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.eventdriven.commonservice.events.OrderCancelledEvent;
import com.eventdriven.commonservice.events.OrderCompletedEvent;
import com.eventdriven.orderservice.command.api.data.Order;
import com.eventdriven.orderservice.command.api.data.OrderRepository;

@Component
public class OrderEventsHandler {

	private OrderRepository orderRepository;

	public OrderEventsHandler(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	@EventHandler
	public void on(OrderCreatedEvent event) {
		Order order = new Order();
		BeanUtils.copyProperties(event, order);
		orderRepository.save(order);
	}

	@EventHandler
	public void on(OrderCompletedEvent event) {
		Optional<Order> orderOptional = orderRepository.findById(event.getOrderId());
		Order order = orderOptional.isPresent() ? orderOptional.get() : null;
		if (Objects.nonNull(order)) {
			order.setOrderStatus(event.getOrderStatus());
			orderRepository.save(order);
		}
		return;
	}

	@EventHandler
	public void on(OrderCancelledEvent event) {
		Optional<Order> orderOptional = orderRepository.findById(event.getOrderId());
		Order order = orderOptional.isPresent() ? orderOptional.get() : null;
		if (Objects.nonNull(order)) {
			order.setOrderStatus(event.getOrderStatus());
			orderRepository.save(order);
		}
		return;
	}
}
