package com.eventdriven.orderservice.command.api.saga;

import java.util.UUID;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

import com.eventdriven.commonservice.commands.CancelOrderCommand;
import com.eventdriven.commonservice.commands.CancelPaymentCommand;
import com.eventdriven.commonservice.commands.CancelShipmentCommand;
import com.eventdriven.commonservice.commands.CompleteOrderCommand;
import com.eventdriven.commonservice.commands.ShipOrderCommand;
import com.eventdriven.commonservice.commands.ValidatePaymentCommand;
import com.eventdriven.commonservice.events.OrderCancelledEvent;
import com.eventdriven.commonservice.events.OrderCompletedEvent;
import com.eventdriven.commonservice.events.OrderShippedEvent;
import com.eventdriven.commonservice.events.PaymentCancelledEvent;
import com.eventdriven.commonservice.events.PaymentProcessedEvent;
import com.eventdriven.commonservice.events.ShipmentCancelledEvent;
import com.eventdriven.commonservice.model.User;
import com.eventdriven.commonservice.queries.GetUserPaymentDetailsQuery;
import com.eventdriven.orderservice.command.api.events.OrderCreatedEvent;

import lombok.extern.slf4j.Slf4j;

@Saga
@Slf4j
public class OrderProcesssingSaga {

	@Autowired
	private transient CommandGateway commandGateway;

	@Autowired
	private transient QueryGateway queryGateway;

	public OrderProcesssingSaga() {
	}

	@StartSaga
	@SagaEventHandler(associationProperty = "orderId")
	private void handle(OrderCreatedEvent event) {
		log.info("OrderCreatedEvent in Saga for Order Id : {}", event.getOrderId());
		GetUserPaymentDetailsQuery getUserPaymentDetailsQuery = new GetUserPaymentDetailsQuery(event.getUserId());
		User user = null;
		try {
			user = queryGateway.query(getUserPaymentDetailsQuery, ResponseTypes.instanceOf(User.class)).join();
		} catch (Exception e) {
			log.error(e.getMessage());
			cancelCommandOrder(event.getOrderId());
		}
		log.info("User : {} ", user);
		ValidatePaymentCommand validPaymentCommand = buildPaymentCommand(event, user);
		commandGateway.sendAndWait(validPaymentCommand);
	}

	@SagaEventHandler(associationProperty = "orderId")
	private void handle(PaymentProcessedEvent event) {
		log.info("PaymentProcessedEvent in Saga for Order Id : {}", event.getOrderId());
		try {
			/*
			 * if (true) throw new Exception();
			 */
			ShipOrderCommand shipOrderCommand = buildShipOrderCommand(event);
			commandGateway.sendAndWait(shipOrderCommand);
		} catch (Exception e) {
			log.error(e.getMessage());
			// Start the compensating transaction
			cancelPaymentCommand(event);
		}
	}

	@SagaEventHandler(associationProperty = "orderId")
	private void handle(OrderShippedEvent event) {
		// Validate OrderShippedEvent
		// publish Complete Order Command
		log.info("OrderShippedEvent in Saga for Order Id : {}", event.getOrderId());
		try {
			CompleteOrderCommand completeOrderCommand = buildCompleteOrderCommand(event);
			commandGateway.send(completeOrderCommand);
		} catch (Exception e) {
			log.error(e.getMessage());
			// Start the compensating transaction
			cancelShipmentCommand(event);
		}
	}

	@SagaEventHandler(associationProperty = "orderId")
	@EndSaga
	public void handle(OrderCompletedEvent event) {
		log.info("OrderCompletedEvent in Saga for Order Id : {}", event.getOrderId());
	}

	@SagaEventHandler(associationProperty = "orderId")
	public void handle(OrderCancelledEvent event) {
		log.info("OrderCancelledEvent in Saga for Order Id : {}", event.getOrderId());
	}

	@SagaEventHandler(associationProperty = "orderId")
	public void handle(PaymentCancelledEvent event) {
		log.info("PaymentCancelledEvent in Saga for Order Id : {}", event.getOrderId());
		cancelCommandOrder(event.getOrderId());
	}

	@SagaEventHandler(associationProperty = "orderId")
	public void handle(ShipmentCancelledEvent event) {
		log.info("ShipmentCancelledEvent in Saga for Order Id : {}", event.getOrderId());
		// To-do Need to handle Payment Cancelled event
		cancelCommandOrder(event.getOrderId());
	}

	private ValidatePaymentCommand buildPaymentCommand(OrderCreatedEvent event, User user) {
		return ValidatePaymentCommand.builder().cardDetails(user.getCardDetails()).orderId(event.getOrderId())
				.paymentId(UUID.randomUUID().toString()).build();
	}

	private void cancelCommandOrder(String orderId) {
		CancelOrderCommand cancelOrderCommand = new CancelOrderCommand(orderId);
		commandGateway.send(cancelOrderCommand);
	}

	private void cancelPaymentCommand(PaymentProcessedEvent event) {
		CancelPaymentCommand cancelPaymentCommand = new CancelPaymentCommand(event.getPaymentId(), event.getOrderId());
		commandGateway.send(cancelPaymentCommand);
	}

	private ShipOrderCommand buildShipOrderCommand(PaymentProcessedEvent event) {
		return ShipOrderCommand.builder().shipmentId(UUID.randomUUID().toString()).orderId(event.getOrderId()).build();
	}

	private void cancelShipmentCommand(OrderShippedEvent event) {
		CancelShipmentCommand cancelShipmentCommand = new CancelShipmentCommand(event.getShipmentId(),
				event.getOrderId());
		commandGateway.send(cancelShipmentCommand);
	}

	private CompleteOrderCommand buildCompleteOrderCommand(OrderShippedEvent event) {
		return CompleteOrderCommand.builder().orderId(event.getOrderId()).orderStatus("APPROVED").build();
	}

}
