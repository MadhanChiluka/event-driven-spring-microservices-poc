package com.eventdriven.productservice.command.api.events;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreatedEvent {
	private String productId;
	private String name;
	private BigDecimal price;
	private Integer quantity;
}
