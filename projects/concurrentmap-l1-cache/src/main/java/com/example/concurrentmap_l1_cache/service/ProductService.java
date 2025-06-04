package com.example.concurrentmap_l1_cache.service;

import com.example.concurrentmap_l1_cache.model.Product;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class ProductService {


    //@Cacheable("products")
    @Cacheable(value = "products", key = "#id")
    public Product getProductById(Long id) {
        simulateSlowService();
        return new Product(id, "Product-" + id, id * 100.0);
        // return repository.findById(id);
    }
    @CachePut(value = "products", key = "#product.id")
    public Product updateProduct(Product product) {
        return product;
       // return repository.save(product);
    }

    @CacheEvict(value = "products", key = "#id")
    public void deleteProduct(Long id) {
       // repository.deleteById(id);
    }
    @CacheEvict(value = "products", allEntries = true)
    public void clearAllProductsCache() {
        // Called to evict entire cache
    }
    @Caching(
            put = { @CachePut(value = "products", key = "#product.id") },
            evict = { @CacheEvict(value = "productList", allEntries = true) }
    )
    public Product saveProduct(Product product) {
        Long id=1L;
        return new Product(id, "Product-" + id, id * 100.0);
      //  return repository.save(product);
    }


    private void simulateSlowService() {
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}