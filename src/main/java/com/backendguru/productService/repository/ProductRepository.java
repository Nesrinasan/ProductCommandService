package com.backendguru.productService.repository;

import com.backendguru.productService.model.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {


}
