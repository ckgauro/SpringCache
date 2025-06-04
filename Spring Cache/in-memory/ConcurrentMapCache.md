### https://github.com/ckgauro/SpringCache/tree/main/projects/concurrentmap-l1-cache

/_
Project: Spring Boot L1 Caching with ConcurrentMapCache
Description: Uses simple ConcurrentMap-based caching (in-memory, per instance).
_/

// 1. pom.xml

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.example</groupId>
    <artifactId>concurrentmap-l1-cache</artifactId>
    <version>1.0.0</version>
    <properties>
        <java.version>21</java.version>
        <spring-boot.version>3.2.5</spring-boot.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-cache</artifactId>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

// 2. application.yml

```yml
spring:
  cache:
    type: simple # Enables ConcurrentMapCache
```

// 3. src/main/java/com/example/l1cache/ConcurrentMapL1Application.java

```java
package com.example.l1cache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ConcurrentMapL1Application {
    public static void main(String[] args) {
        SpringApplication.run(ConcurrentMapL1Application.class, args);
    }
}
```

// 4. src/main/java/com/example/l1cache/model/Product.java

```java
package com.example.l1cache.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private Long id;
    private String name;
    private double price;
}
```

// 5. src/main/java/com/example/l1cache/service/ProductService.java

```java
package com.example.l1cache.service;

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
```

// 6. src/main/java/com/example/l1cache/controller/ProductController.java

```java
package com.example.l1cache.controller;

import com.example.l1cache.model.Product;
import com.example.l1cache.service.ProductService;
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
}
```

// 7. Test

```
// Start application and access: http://localhost:8080/products/1
// First call takes ~3s, subsequent calls are near-instant (L1 cache)
// Cache is per-instance and non-persistent.
http://localhost:8080/products/2
http://localhost:8080/products/3
```

# 🔁 Spring Cache Annotations for CRUD Operations

| Annotation    | Purpose                             | Typical Use Case                         |
| ------------- | ----------------------------------- | ---------------------------------------- |
| `@Cacheable`  | Read (load into cache if not found) | Fetch and cache method result            |
| `@CachePut`   | Update (always invoke method)       | Update the cache without skipping method |
| `@CacheEvict` | Delete (remove entry from cache)    | Invalidate/delete cache entries          |
| `@Caching`    | Compose multiple cache annotations  | Mix of `@CachePut` + `@CacheEvict` etc.  |

[📚 Documentation ↗](https://docs.spring.io/spring-boot/docs/current/reference/html/)
