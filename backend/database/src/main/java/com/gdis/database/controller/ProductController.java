package com.gdis.database.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import com.gdis.database.model.Product;
import com.gdis.database.service.ProductRepository;
import com.gdis.database.util.PreCondition;

@RestController
@RequestMapping("/products")
public class ProductController {
	
	
	@Autowired
	private ProductRepository repository;

	
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

	/*
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
	*/
	
	@RequestMapping(method = RequestMethod.POST, consumes = { MediaType.APPLICATION_XML_VALUE }, produces = {
			MediaType.APPLICATION_XML_VALUE })
	public Product createProduct(final Product product) {
		PreCondition.notNull(product, Product.class.getSimpleName());

		final Product createdProduct = repository.save(product);

		return createdProduct;
	}
	
	
}
