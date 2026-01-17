package com.instamart.instamartapp.products.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.instamart.instamartapp.products.model.Product;

public interface ProductRepository extends MongoRepository<Product, String> {
}
