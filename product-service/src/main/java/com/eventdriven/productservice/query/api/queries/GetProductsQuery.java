package com.eventdriven.productservice.query.api.queries;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class GetProductsQuery {
	private String productId;
	private String name;
	private BigDecimal price;
	private Integer quantity;
}
