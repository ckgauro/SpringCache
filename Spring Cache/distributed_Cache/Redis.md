# 🔴 Redis — Overview and Key Details

---

## What is Redis?

**Redis** (REmote DIctionary Server) is an open-source, in-memory data structure store used as a database, cache, and message broker. It is designed for ultra-fast data access and supports various advanced data types beyond simple key-value pairs.

---

## Key Features of Redis

- **In-Memory Storage**: All data is stored in RAM, enabling extremely low latency and high throughput.
- **Data Structures**: Supports strings, hashes, lists, sets, sorted sets, bitmaps, hyperloglogs, streams, and more.
- **Persistence Options**: Allows snapshotting (RDB) and append-only file (AOF) persistence to disk, combining speed with durability.
- **Replication & High Availability**: Supports master-slave replication, automatic failover with Redis Sentinel, and clustering for horizontal scaling.
- **Transactions**: Provides atomic operations through MULTI/EXEC commands.
- **Pub/Sub Messaging**: Enables message broadcasting with publish/subscribe pattern.
- **Lua Scripting**: Allows running atomic scripts inside Redis server.
- **Eviction Policies**: Supports configurable cache eviction strategies like LRU (Least Recently Used).

---

## Why Use Redis for Caching?

- **Performance**: Extremely fast reads and writes due to in-memory design.
- **Scalability**: Supports clustering and sharding, allowing distributed caching across multiple nodes.
- **Feature-Rich**: Beyond simple key-value caching, you can cache complex objects and perform atomic updates.
- **TTL Support**: Automatic expiration of cached entries via configurable Time-To-Live (TTL).
- **Integration**: Well-supported in the Java ecosystem, including Spring Framework with `spring-data-redis`.

---

## Redis in Spring Framework

Spring Boot provides seamless integration with Redis caching through the `spring-boot-starter-data-redis` starter.

### Example Configuration

```java
@Configuration
@EnableCaching
public class RedisCacheConfig {

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory factory) {
        return RedisCacheManager.builder(factory)
                                .cacheDefaults(RedisCacheConfiguration.defaultCacheConfig()
                                                                    .entryTtl(Duration.ofMinutes(10)))
                                .build();
    }
}
```

### Sample Usage

```java
@Cacheable(value = "products", key = "#id")
public Product getProductById(Long id) {
    // Simulate slow DB call
    return productRepository.findById(id).orElseThrow();
}
```

## Use Cases

- Session storage in web applications.
- Caching frequently accessed database query results.
- Leaderboards or real-time counters using sorted sets.
- Rate limiting API requests.
- Distributed locking via Redis keys.

## Limitations

- Being in-memory, it requires sufficient RAM for data size.
- Single-threaded architecture can be a bottleneck for some workloads.
- Requires additional setup for persistence and high availability.

## Summary

| Aspect          | Description                                               |
| --------------- | --------------------------------------------------------- |
| Type            | In-memory key-value data store                            |
| Persistence     | RDB snapshots & AOF logs                                  |
| Data Structures | Strings, hashes, lists, sets, sorted sets, streams, etc.  |
| Scaling         | Replication, clustering, sharding                         |
| Integration     | Native support in Spring via `spring-data-redis`          |
| Typical Uses    | Caching, session management, pub/sub, real-time analytics |

# Spring Boot 3.4.2 + JDK 21: Redis Caching Detailed Example

This guide demonstrates how to integrate **Redis caching** with a Spring Boot 3.4.2 application using JDK 21.

---

## 🛠 Prerequisites

- JDK 21 installed
- Redis server running locally on default port 6379 (or configured accordingly)
- Spring Boot 3.4.2 project setup (Maven or Gradle)

---

## 1. Add Dependencies

### Maven (`pom.xml`)

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-cache</artifactId>
</dependency>

<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

## 2. Configure Redis Cache in Spring Boot

Create a configuration class to enable caching and configure Redis `CacheManager`:

```java
package com.example.redisconfig;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.time.Duration;

@Configuration
@EnableCaching
public class RedisCacheConfig {

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofMinutes(15))         // Set cache TTL to 15 minutes
            .disableCachingNullValues();               // Avoid caching nulls

        return RedisCacheManager.builder(connectionFactory)
                                .cacheDefaults(config)
                                .build();
    }
}
```

### ☁️ Spring Boot Redis Caching Example (Spring Boot 3.4.2 + JDK 21)

---

### 📁 3. Application Properties

**application.yml**

```yml
spring:
  redis:
    host: localhost
    port: 6379
  cache:
    type: redis
```

## 📦 4. Create a Sample Entity and Repository

### `Product.java`

```java
package com.example.model;

public record Product(Long id, String name, double price) { }
```

`ProductRepository.java`

```java
package com.example.repository;

import com.example.model.Product;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class ProductRepository {
    private static final Map<Long, Product> PRODUCTS = new HashMap<>();

    static {
        PRODUCTS.put(1L, new Product(1L, "Laptop", 1500.00));
        PRODUCTS.put(2L, new Product(2L, "Smartphone", 900.00));
    }

    public Optional<Product> findById(Long id) {
        simulateSlowDatabaseCall();
        return Optional.ofNullable(PRODUCTS.get(id));
    }

    private void simulateSlowDatabaseCall() {
        try {
            Thread.sleep(2000); // Simulate delay
        } catch (InterruptedException ignored) { }
    }
}
```

## 💼 5. Service Layer with Caching

**`ProductService.java`**

```java
package com.example.service;

import com.example.model.Product;
import com.example.repository.ProductRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository repo) {
        this.productRepository = repo;
    }

    @Cacheable(value = "products", key = "#id")
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }
}

```

## 🌐 6. Controller to Test Caching

**`ProductController.java`**

```java
package com.example.controller;

import com.example.model.Product;
import com.example.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService svc) {
        this.productService = svc;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {
        return productService.getProductById(id)
                             .map(ResponseEntity::ok)
                             .orElse(ResponseEntity.notFound().build());
    }
}
```

#### 7. Running and Testing

**Steps:**

1. Start Redis server locally (e.g., `redis-server` command).
2. Run your Spring Boot application.
3. Access the API endpoint: `GET http://localhost:8080/api/products/1`
   - The first request will be slow (~2 seconds).
   - Subsequent requests to the same ID will be fast (cache hit).
4. Monitor Redis keys using `redis-cli` command:

```bash
redis-cli keys '*'
```

You should see keys like `products::1` representing cached entries

#### 8. Optional: Cache Eviction Example

Add a method to clear the cache when product is updated or deleted:

```java
@CacheEvict(value = "products", key = "#id")
public void evictProductCache(Long id) {
// logic to update or delete product
}
```

## 📋 Summary

- **Spring Boot 3.4.2 + JDK 21** supports Redis caching out-of-the-box.
- Enable caching with `@EnableCaching` and configure a `RedisCacheManager` for Redis-backed caches.
- Use `@Cacheable`, `@CacheEvict`, and `@CachePut` annotations to control cache behavior.
- Redis stores cached data with **TTL** (time-to-live) for efficient memory usage and automatic eviction.
