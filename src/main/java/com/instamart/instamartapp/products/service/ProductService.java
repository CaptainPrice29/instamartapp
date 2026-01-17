package com.instamart.instamartapp.products.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.instamart.instamartapp.products.model.Product;
import com.instamart.instamartapp.products.repository.ProductRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(Product product, String productId) {
        Product existingProduct = productRepository.findById(productId).orElse(null);
        if (existingProduct != null) {
            Product updatedProduct = existingProduct.toBuilder()
                    .name(product.getName())
                    .description(product.getDescription())
                    .price(product.getPrice())
                    .discountPrice(product.getDiscountPrice())
                    .currency(product.getCurrency())
                    .stockQuantity(product.getStockQuantity())
                    .isAvailable(product.isAvailable())
                    .category(product.getCategory())
                    .brand(product.getBrand())
                    .image(product.getImage())
                    .images(product.getImages())
                    .tags(product.getTags())
                    .features(product.getFeatures())
                    .attributes(product.getAttributes())
                    .rating(product.getRating())
                    .ratingCount(product.getRatingCount())
                    .build();
            return productRepository.save(updatedProduct);
        }
        return null;
    }

    public void deleteProduct(String productId) {
        productRepository.deleteById(productId);
    }

    public Product getProductById(String productId) {
        return productRepository.findById(productId).orElse(null);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Page<Product> getFilteredProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

}
