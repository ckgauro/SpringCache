package com.example.concurrentmap_l1_cache.model;

import com.example.l1cache.model.Product;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class ProductService {

    @Cacheable("products")
    public Product getProductById(Long id) {
        simulateSlowService();
        return new Product(id, "Product-" + id, id * 100.0);
    }

    private void simulateSlowService() {
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}