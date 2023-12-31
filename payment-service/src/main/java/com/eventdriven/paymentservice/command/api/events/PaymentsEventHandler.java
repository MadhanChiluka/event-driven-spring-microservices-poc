package com.eventdriven.paymentservice.command.api.events;

import java.util.Date;

import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

import com.eventdriven.commonservice.events.PaymentCancelledEvent;
import com.eventdriven.commonservice.events.PaymentProcessedEvent;
import com.eventdriven.paymentservice.command.api.data.Payment;
import com.eventdriven.paymentservice.command.api.data.PaymentRepository;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PaymentsEventHandler {

	private PaymentRepository paymentRepository;

	public PaymentsEventHandler(PaymentRepository paymentRepository) {
		this.paymentRepository = paymentRepository;
	}

	@EventHandler
	public void on(PaymentProcessedEvent event) {
		log.info("PaymentProcessedEvent : {}", event.getPaymentId());
		Payment payment = Payment.builder().paymentId(event.getPaymentId()).orderId(event.getOrderId())
				.timeStamp(new Date()).paymentStatus("COMPLETED").build();
		paymentRepository.save(payment);
	}

	@EventHandler
	public void on(PaymentCancelledEvent event) {
		Payment payment = paymentRepository.findById(event.getPaymentId()).get();
		payment.setPaymentStatus(event.getPaymentStatus());
		paymentRepository.save(payment);
	}
}
