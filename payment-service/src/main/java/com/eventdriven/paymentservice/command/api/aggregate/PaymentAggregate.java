package com.eventdriven.paymentservice.command.api.aggregate;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import com.eventdriven.commonservice.commands.CancelPaymentCommand;
import com.eventdriven.commonservice.commands.ValidatePaymentCommand;
import com.eventdriven.commonservice.events.PaymentCancelledEvent;
import com.eventdriven.commonservice.events.PaymentProcessedEvent;

import lombok.extern.slf4j.Slf4j;

@Aggregate
@Slf4j
public class PaymentAggregate {
	@AggregateIdentifier
	private String paymentId;
	private String orderId;
	private String paymentStatus;

	public PaymentAggregate() {
	}

	@CommandHandler
	public PaymentAggregate(ValidatePaymentCommand validatePaymentCommand) {
		// Validate the payment Details
		// Publish Payment Processed Event
		log.info("Executing ValidatePaymentCommand for Order Id : {} and Payment Id : {} ",
				validatePaymentCommand.getOrderId(), validatePaymentCommand.getPaymentId());
		PaymentProcessedEvent paymentProcessedEvent = new PaymentProcessedEvent(validatePaymentCommand.getOrderId(),
				validatePaymentCommand.getPaymentId());
		AggregateLifecycle.apply(paymentProcessedEvent);
		log.info("PaymentProcessedEvent Applied");
	}

	@EventSourcingHandler
	public void on(PaymentProcessedEvent event) {
		this.paymentId = event.getPaymentId();
		this.orderId = event.getOrderId();
	}

	@CommandHandler
	public void handle(CancelPaymentCommand cancelPaymentCommand) {
		PaymentCancelledEvent paymentCancelledEvent = new PaymentCancelledEvent();
		BeanUtils.copyProperties(cancelPaymentCommand, paymentCancelledEvent);
		AggregateLifecycle.apply(paymentCancelledEvent);
	}

	@EventSourcingHandler
	public void on(PaymentCancelledEvent event) {
		this.paymentStatus = event.getPaymentStatus();
	}
}
