package com.eventdriven.orderservice.command.api.controller;

import java.util.UUID;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eventdriven.orderservice.command.api.command.CreateOrderCommand;
import com.eventdriven.orderservice.command.api.model.OrderRestModel;

@RestController
@RequestMapping("/orders")
public class OrderCommandController {
	private CommandGateway commandGateway;

	public OrderCommandController(CommandGateway commandGateway) {
		this.commandGateway = commandGateway;
	}

	@PostMapping
	public String createOrder(@RequestBody OrderRestModel orderRestModel) {
		CreateOrderCommand createOrderCommand = mapRestModelToCommand(orderRestModel);
		commandGateway.sendAndWait(createOrderCommand);
		return "Order Created";
	}

	private CreateOrderCommand mapRestModelToCommand(OrderRestModel orderRestModel) {
		return CreateOrderCommand.builder().orderId(UUID.randomUUID().toString())
				.addressId(orderRestModel.getAddressId()).productId(orderRestModel.getProductId())
				.quantity(orderRestModel.getQuantity()).userId(orderRestModel.getUserId()).orderStatus("CREATED")
				.build();
	}
}
