package com.example.concurrentmap_l1_cache.controller;

import com.example.concurrentmap_l1_cache.model.Product;
import com.example.concurrentmap_l1_cache.service.ProductService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
         productService.deleteProduct(id);
    }

}
