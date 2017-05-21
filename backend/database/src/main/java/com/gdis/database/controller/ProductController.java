package com.gdis.database.controller;


import java.util.ArrayList;
import java.util.List;
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
@RequestMapping("/db/products")
public class ProductController {


        @Autowired
        private ProductRepository productRepository;


        @RequestMapping(method = RequestMethod.GET)
        public ResponseEntity<?> getAllProducts() {
        	
        	Iterable<Product> productIterable = productRepository.findAll();
    		
    		if(productIterable == null) {
    			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    		}
    		
    		List<Product> productList = new ArrayList<Product>();
    	
    		// Java 8 Method Reference is used here
    		productIterable.forEach(productList::add);
    	
    		return new ResponseEntity<>(productList, HttpStatus.OK);
        }

        
        @RequestMapping(value = "/{id}", method = RequestMethod.GET)
        public ResponseEntity<Product> getProduct(@PathVariable long id) {
                PreCondition.require(id > 0, "id must be greater than 0 ");

                final Product product = productRepository.findById(id);

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

        @RequestMapping(value = "/insert", method = RequestMethod.POST, 
        		consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
        public ResponseEntity<?> createProduct(final Product product) {
                PreCondition.notNull(product, Product.class.getSimpleName());

                //final Product createdProduct = productRepository.save(product);

                return new ResponseEntity<>(HttpStatus.OK);
        }


}