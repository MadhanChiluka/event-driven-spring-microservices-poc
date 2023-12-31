package com.eventdriven.shipmentservice.command.api.aggregate;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import com.eventdriven.commonservice.commands.CancelShipmentCommand;
import com.eventdriven.commonservice.commands.ShipOrderCommand;
import com.eventdriven.commonservice.events.OrderShippedEvent;
import com.eventdriven.commonservice.events.ShipmentCancelledEvent;

import lombok.extern.slf4j.Slf4j;

@Aggregate
@Slf4j
public class ShipmentAggregate {

	@AggregateIdentifier
	private String shipmentId;
	private String orderId;
	private String shipmentStatus;

	public ShipmentAggregate() {
	}

	@CommandHandler
	public ShipmentAggregate(ShipOrderCommand shipOrderCommand) {
		// Validate ShipOrderCommand
		// publish OrderShippedEvent
		OrderShippedEvent orderShippedEvent = buildOrderShippedEvent(shipOrderCommand);
		AggregateLifecycle.apply(orderShippedEvent);
		log.info("OrderShippedEvent Completed");
	}

	@EventSourcingHandler
	public void on(OrderShippedEvent event) {
		this.orderId = event.getOrderId();
		this.shipmentId = event.getShipmentId();
		this.shipmentStatus = event.getShipmentStatus();
	}

	private OrderShippedEvent buildOrderShippedEvent(ShipOrderCommand shipOrderCommand) {
		return OrderShippedEvent.builder().shipmentId(shipOrderCommand.getShipmentId())
				.orderId(shipOrderCommand.getOrderId()).shipmentStatus("APPROVED").build();
	}

	@CommandHandler
	public void handle(CancelShipmentCommand cancelShipmentCommand) {
		ShipmentCancelledEvent shipmentCancelledEvent = new ShipmentCancelledEvent();
		BeanUtils.copyProperties(cancelShipmentCommand, shipmentCancelledEvent);
		AggregateLifecycle.apply(shipmentCancelledEvent);
	}

	@EventSourcingHandler
	public void on(ShipmentCancelledEvent event) {
		this.shipmentStatus = event.getShipmentStatus();
	}
}
