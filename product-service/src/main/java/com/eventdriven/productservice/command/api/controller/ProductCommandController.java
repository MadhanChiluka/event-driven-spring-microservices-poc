package com.eventdriven.productservice.command.api.controller;

import java.util.UUID;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eventdriven.productservice.command.api.commands.CreateProductCommand;
import com.eventdriven.productservice.command.api.model.ProductRestModel;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/products")
@AllArgsConstructor
public class ProductCommandController {

	private CommandGateway commandGateway;

	@PostMapping
	public String addProduct(@RequestBody ProductRestModel productRestModel) {
		CreateProductCommand createProductCommand = buildCreateProductCmd(productRestModel);
		return commandGateway.sendAndWait(createProductCommand);
	}

	private CreateProductCommand buildCreateProductCmd(ProductRestModel productRestModel) {
		return CreateProductCommand.builder().productId(UUID.randomUUID().toString()).name(productRestModel.getName())
				.price(productRestModel.getPrice()).quantity(productRestModel.getQuantity()).build();
	}
}
