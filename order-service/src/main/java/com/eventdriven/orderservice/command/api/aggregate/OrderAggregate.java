package com.eventdriven.orderservice.command.api.aggregate;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import com.eventdriven.commonservice.commands.CancelOrderCommand;
import com.eventdriven.commonservice.commands.CompleteOrderCommand;
import com.eventdriven.commonservice.events.OrderCancelledEvent;
import com.eventdriven.commonservice.events.OrderCompletedEvent;
import com.eventdriven.orderservice.command.api.command.CreateOrderCommand;
import com.eventdriven.orderservice.command.api.events.OrderCreatedEvent;

@Aggregate
public class OrderAggregate {

	@AggregateIdentifier
	private String orderId;
	private String productId;
	private String userId;
	private String addressId;
	private Integer quantity;
	private String orderStatus;

	public OrderAggregate() {

	}

	@CommandHandler
	public OrderAggregate(CreateOrderCommand createOrderCommand) {
		// Validate The Command
		OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent();
		BeanUtils.copyProperties(createOrderCommand, orderCreatedEvent);
		AggregateLifecycle.apply(orderCreatedEvent);
	}

	@EventSourcingHandler
	public void on(OrderCreatedEvent orderCreatedEvent) {
		this.orderStatus = orderCreatedEvent.getOrderStatus();
		this.userId = orderCreatedEvent.getUserId();
		this.orderId = orderCreatedEvent.getOrderId();
		this.quantity = orderCreatedEvent.getQuantity();
		this.productId = orderCreatedEvent.getProductId();
		this.addressId = orderCreatedEvent.getAddressId();
	}

	@CommandHandler
	public void on(CompleteOrderCommand completeOrderCommand) {
		// Validate The Command
		// Publish the Order Completed Event
		OrderCompletedEvent orderCompletedEvent = buildOrderCompletedEvent(completeOrderCommand);
		AggregateLifecycle.apply(orderCompletedEvent);
	}

	@EventSourcingHandler
	public void on(OrderCompletedEvent event) {
		this.orderStatus = event.getOrderStatus();
	}

	private OrderCompletedEvent buildOrderCompletedEvent(CompleteOrderCommand completeOrderCommand) {
		return OrderCompletedEvent.builder().orderId(completeOrderCommand.getOrderId())
				.orderStatus(completeOrderCommand.getOrderStatus()).build();
	}

	@CommandHandler
	public void handle(CancelOrderCommand cancelOrderCommand) {
		OrderCancelledEvent orderCancelledEvent = new OrderCancelledEvent();
		BeanUtils.copyProperties(cancelOrderCommand, orderCancelledEvent);
		AggregateLifecycle.apply(orderCancelledEvent);
	}

	@EventSourcingHandler
	public void on(OrderCancelledEvent orderCancelledEvent) {
		this.orderStatus = orderCancelledEvent.getOrderStatus();
	}

}
