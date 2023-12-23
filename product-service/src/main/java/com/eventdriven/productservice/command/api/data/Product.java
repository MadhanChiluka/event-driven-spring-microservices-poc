package com.eventdriven.productservice.command.api.data;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Product {
	@Id
	private String productId;
	private String name;
	private BigDecimal price;
	private Integer quantity;
}
