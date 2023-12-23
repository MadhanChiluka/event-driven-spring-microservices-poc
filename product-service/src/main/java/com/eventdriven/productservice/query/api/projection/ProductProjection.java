package com.eventdriven.productservice.query.api.projection;

import java.util.List;
import java.util.stream.Collectors;

import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import com.eventdriven.productservice.command.api.data.Product;
import com.eventdriven.productservice.command.api.data.ProductRepository;
import com.eventdriven.productservice.command.api.model.ProductRestModel;
import com.eventdriven.productservice.query.api.queries.GetProductsQuery;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class ProductProjection {
	
	private ProductRepository productRepository;
	
	@QueryHandler
	public List<ProductRestModel> handle(GetProductsQuery getProductsQuery) {
		List<Product> products = productRepository.findAll();
		return mapProductToRestModel(products);
	}

	private List<ProductRestModel> mapProductToRestModel(List<Product> products) {
		return products.stream().map(product -> {
			return ProductRestModel.builder().name(product.getName()).price(product.getPrice())
					.quantity(product.getQuantity()).build();
		}).toList();
	}

}
