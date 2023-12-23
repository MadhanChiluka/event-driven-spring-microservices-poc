package com.eventdriven.productservice.query.api;

import java.util.List;

import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eventdriven.productservice.command.api.model.ProductRestModel;
import com.eventdriven.productservice.query.api.queries.GetProductsQuery;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/products")
@AllArgsConstructor
public class ProductQueryController {

	private QueryGateway queryGateway;

	@GetMapping
	public List<ProductRestModel> getAllProducts() {
		GetProductsQuery getProductsQuery = new GetProductsQuery();
		return queryGateway.query(getProductsQuery, 
				ResponseTypes.multipleInstancesOf(ProductRestModel.class)).join();

	}
}
