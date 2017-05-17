package com.gdis.database.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gdis.database.models.Contract;
import com.gdis.database.models.Product;
import com.gdis.database.services.ProductRepository;
import util.PreCondition;
//import io.swagger.annotations.ApiOperation;
//import io.swagger.annotations.ApiResponse;
//import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/products")
public class ProductController {
	@Autowired
	private ProductRepository repository;

	/*
    @ApiOperation(value = "getAllProducts", nickname = "getAllProducts")
    @ApiResponses(value = { 
            @ApiResponse(code = 200, message = "Success", response = Product.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")}) */
	@RequestMapping(method = RequestMethod.GET, produces = { MediaType.APPLICATION_XML_VALUE })
	public Iterable<Product> getAllProducts() {
		Iterable<Product> products = repository.findAll();

		return products;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<Product> getProduct(@PathVariable long id) {
		PreCondition.require(id > 0, "id must be greater than 0 ");

		final Product product = repository.findById(id);
		
		return ResponseEntity
				.status(HttpStatus.OK)
				.contentType(MediaType.APPLICATION_XML)
				.body(product);
	}

	@RequestMapping(value = "/{id}/contracts", method = RequestMethod.GET, produces = { MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<Iterable<Contract>> getContractsForProduct(@PathVariable long id) {
	PreCondition.require(id > 0, "id must be greater than 0");

		final Product product = repository.findById(id);
		final List<Contract> contracts = product.getContracts();
		
		return ResponseEntity
				.status(HttpStatus.OK)
				.contentType(MediaType.APPLICATION_XML)
				.body(contracts);
	}
	
	@RequestMapping(method = RequestMethod.POST, consumes = { MediaType.APPLICATION_XML_VALUE }, produces = {
			MediaType.APPLICATION_XML_VALUE })
	public Product createProduct(final Product product) {
		PreCondition.notNull(product, Product.class.getSimpleName());

		final Product createdProduct = repository.save(product);

		return createdProduct;
	}
}
