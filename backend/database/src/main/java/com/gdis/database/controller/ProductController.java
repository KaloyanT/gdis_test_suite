package com.gdis.database.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.gdis.database.service.ProductRepository;

@RestController
@RequestMapping("/db/products")
public class ProductController {
	
	
	@Autowired
	private ProductRepository repository;
	
	
}
