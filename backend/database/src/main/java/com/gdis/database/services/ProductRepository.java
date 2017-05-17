package com.gdis.database.services;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import com.gdis.database.models.Product;

public interface ProductRepository extends CrudRepository <Product, Long> {
	List<Product> findByName(String name);
	
	public Product findById(long id);
}
