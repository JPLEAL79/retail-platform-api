package com.jp.testplatformapi.service;

import com.jp.testplatformapi.entity.Product;
import com.jp.testplatformapi.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public List<Product> getAll() {
        return repository.findAll();
    }

    public Product create(Product product) {
        return repository.save(product);
    }
}