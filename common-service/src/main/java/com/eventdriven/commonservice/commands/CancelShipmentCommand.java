package com.eventdriven.commonservice.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.Value;

@Value
public class CancelShipmentCommand {
	@TargetAggregateIdentifier
	private String shipmentId;
	private String orderId;
	private String shipmentStatus = "CANCELLED";
}
