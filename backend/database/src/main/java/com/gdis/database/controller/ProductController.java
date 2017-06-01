package com.gdis.database.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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

        
	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
	public ResponseEntity<Product> getProduct(@PathVariable(value = "id") final long id) {
                
    	 PreCondition.require(id >= 0, "Product ID can't be negative!");

    	 Product response = productRepository.findByProductID(id);
                
    	 if(response == null) {
    		 return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    	 }
                
    	 return new ResponseEntity<>(response, HttpStatus.OK);
     }

     
	@RequestMapping(value = "/insert", method = RequestMethod.POST, 
    		 consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
	public ResponseEntity<?> createProduct(@RequestBody Product newProduct) {
                
		if(newProduct == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		// Check if the given customer already exists in the customers table of the DB
		// If so, don't insert the customer again in the customers table
		List<Product> productsWithTheSameNameAndType = 
			productRepository.findByNameAndProductBeginAndProductEndAndProductType(newProduct.getName(), 
			newProduct.getProductBegin(), newProduct.getProductEnd(), newProduct.getProductType());
				
		if(newProduct.productExistsInDB(productsWithTheSameNameAndType) > 0) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}

		productRepository.save(newProduct);

		return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT, 
    		 consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE}) 
	public ResponseEntity<?> updateContract(@PathVariable("id") final long id, @RequestBody Product updatedProduct) {
    		
		PreCondition.require(id >= 0, "Product ID can't be negative!");
    		
		if(updatedProduct == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
    		
		Product currentProduct = productRepository.findByProductID(id);
    		
		if(currentProduct == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
        	
		updatedProduct.setProductID(id);
    		
		productRepository.save(updatedProduct);
        	
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}
     
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteProduct(@PathVariable("id") final long id) {
 		
		PreCondition.require(id >= 0, "Product ID can't be negative!");
 		
		Product toBeDeleted = productRepository.findByProductID(id);
 		
		if(toBeDeleted == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
 		
		productRepository.deleteById(id);
 		
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
