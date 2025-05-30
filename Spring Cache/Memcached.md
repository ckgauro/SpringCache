# 🟦 Memcached: A Lightweight Distributed Caching System

## 📌 Overview

**Memcached** is a high-performance, **in-memory**, **distributed** key-value store designed for **caching data** and reducing the load on databases or APIs. It is **simple**, **fast**, and widely used in web-scale applications for storing short-lived data such as session info, rendered views, and API responses.

---

## 🚀 Key Features

| Feature         | Description                                           |
| --------------- | ----------------------------------------------------- |
| In-Memory Only  | All data is stored in RAM, enabling ultra-fast access |
| Key-Value Store | Stores data as string-based key-value pairs           |
| Distributed     | Supports horizontal scaling with consistent hashing   |
| Multi-threaded  | Can handle concurrent requests efficiently            |
| LRU Eviction    | Least Recently Used policy when memory is full        |
| Stateless       | No persistence, no replication                        |

---

## 🛠️ Use Cases

- Caching database query results
- Session caching in distributed systems
- Reducing load on APIs or microservices
- Front-end page fragment caching

---

## ❌ Limitations

- **No persistence**: Data is lost on restart
- **Basic data model**: Only strings or byte arrays (no rich data structures)
- **No built-in replication or clustering**
- Not suitable for **mission-critical or long-lived** data

---

## ✅ Advantages

- Extremely fast and efficient
- Easy to install and configure
- Widely supported by many frameworks
- Ideal for ephemeral, frequently accessed data

---

## 🧪 Sample Memcached Setup in Spring (via XMemcached or Spymemcached)

> Spring doesn't provide out-of-the-box Memcached support — use third-party libraries.

### 1. Add XMemcached Dependency (Maven)

```xml
<dependency>
  <groupId>com.googlecode.xmemcached</groupId>
  <artifactId>xmemcached</artifactId>
  <version>2.4.7</version>
</dependency>
```

## 📊 Memcached vs Redis (Quick View)

| Feature         | Memcached    | Redis           |
| --------------- | ------------ | --------------- |
| Persistence     | ❌ No        | ✅ Optional     |
| Data Structures | ❌ Keys only | ✅ Rich types   |
| Pub/Sub         | ❌           | ✅              |
| Memory Usage    | ✅ Efficient | ❌ Higher       |
| Complexity      | ✅ Simple    | ❌ More complex |

### 📘 Useful Commands (CLI)

```bash
telnet localhost 11211
> set user:123 300 0 15
> get user:123
> delete user:123
```

## 📦 Resources

- [Memcached](https://memcached.org)
- [XMemcached (GitHub)](https://github.com/killme2008/xmemcached)
- [Simple Spring Memcached](https://github.com/igor-suhorukov/simple-spring-memcached)

## 📦 Project Setup

### 1. Add Dependencies

In your `pom.xml`, include the necessary dependencies:

```xml
<dependencies>
    <!-- Spring Boot Starter -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter</artifactId>
    </dependency>

    <!-- Spring Boot Starter Web -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!-- Spring Boot Starter Cache -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-cache</artifactId>
    </dependency>

    <!-- Simple Spring Memcached -->
    <dependency>
        <groupId>com.google.code.simple-spring-memcached</groupId>
        <artifactId>spring-cache</artifactId>
        <version>4.1.3</version>
    </dependency>
    <dependency>
        <groupId>com.google.code.simple-spring-memcached</groupId>
        <artifactId>xmemcached-provider</artifactId>
        <version>4.1.3</version>
    </dependency>
    <dependency>
        <groupId>com.googlecode.xmemcached</groupId>
        <artifactId>xmemcached</artifactId>
        <version>2.4.6</version>
    </dependency>
</dependencies>
```

_Note_: Ensure that the versions are compatible with _Spring 3.4.2_ and _JDK 21_.

### 2. Enable Caching

Create a configuration class to enable caching:

```java
package com.example.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {
// Additional configurations if necessary
}
```

### 3. Configure Memcached

Set up the Memcached client configuration:

```java
package com.example.config;

import com.google.code.ssm.CacheFactory;
import com.google.code.ssm.Cache;
import com.google.code.ssm.config.DefaultAddressProvider;
import com.google.code.ssm.providers.xmemcached.MemcacheClientFactoryImpl;
import com.google.code.ssm.providers.xmemcached.XMemcachedConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class MemcachedConfig {

    @Bean
    public CacheFactory defaultCacheFactory() {
        CacheFactory cacheFactory = new CacheFactory();
        cacheFactory.setCacheClientFactory(new MemcacheClientFactoryImpl());
        cacheFactory.setAddressProvider(new DefaultAddressProvider("localhost:11211"));

        XMemcachedConfiguration configuration = new XMemcachedConfiguration();
        configuration.setConsistentHashing(true);
        configuration.setUseBinaryProtocol(true);
        cacheFactory.setConfiguration(configuration);

        cacheFactory.setCaches(Collections.singletonList("defaultCache"));
        return cacheFactory;
    }

}
```

Tip: Replace `"localhost:11211"` with your Memcached server address if different.

### 🧩 Service Implementation

### 4. Create a Service with Caching

Implement a service that utilizes caching:

```java
package com.example.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Cacheable(value = "defaultCache", key = "#id")
    public String getProductById(String id) {
        simulateSlowService();
        return "Product-" + id;
    }

    private void simulateSlowService() {
        try {
            Thread.sleep(3000L); // Simulate delay
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

}
```

_Explanation_:
`@Cacheable` caches the result of `getProductById`. Subsequent calls with the same `id` will bypass the method and return the cached value.

## 🚀 Running the Application

### 5. Start Memcached

Ensure that Memcached is running. You can start it using Docker:

```bash
docker run -d -p 11211:11211 memcached
```

## 🚀 6. Run the Spring Boot Application

Use Maven to build and run the application:

```bash
mvn spring-boot:run
```

### 🧪 7. Invoke the Service Endpoint

Assuming you have a REST controller exposing the `getProductById` method:

```java
package com.example.controller;

import com.example.service.ProductService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{id}")
    public String getProduct(@PathVariable String id) {
        return productService.getProductById(id);
    }
}
```

Test with:

```bash
curl http://localhost:8080/products/123
```

### 🧭 Response Time

- _First call_: ~3 seconds (due to simulated delay)
- _Subsequent calls_: Instant (retrieved from cache)
