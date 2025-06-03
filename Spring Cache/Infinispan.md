# 🧩 Infinispan - Distributed Cache & Data Grid for Java

## 📘 What is Infinispan?

**Infinispan** is a **distributed in-memory key-value data store and data grid** developed by Red Hat. It supports both **embedded** and **client-server** modes, making it highly flexible for Java-based applications needing fast, scalable, and resilient caching or data grid solutions.

---

## 🚀 Key Features

| Feature                       | Description                                                          |
| ----------------------------- | -------------------------------------------------------------------- |
| 💾 In-memory + persistent     | Stores data in memory with optional disk persistence                 |
| 🌍 Clustering                 | Built-in support for clustering and replication                      |
| 📦 JCache (JSR-107)           | Full support for standard Java Caching API                           |
| 🔁 Data eviction + expiration | Supports LRU, LFU, FIFO, and time-based expiration                   |
| 🔐 Security & TLS             | Role-based access control, TLS encryption                            |
| ☁️ Cloud Native               | Kubernetes, OpenShift integration, REST, HotRod, and JDBC interfaces |
| 📊 Query Support              | Supports SQL, Ickle (a JPQL-like query language), indexing (Lucene)  |
| 🧪 Transactions               | ACID transactions (XA, two-phase commit, etc.)                       |

---

## 🧠 How Infinispan Works

Infinispan can be used in two modes:

### 1. **Embedded Mode**

- Embedded directly in the JVM
- Fastest option (low latency, in-process access)
- Ideal for single-node or tightly-coupled systems

```java
Cache<String, String> cache = cacheManager.getCache("myCache");
cache.put("key1", "value1");
```

# 🧠 Infinispan Overview

## 📂 Common Use Cases

- Microservices caching
- Distributed session management
- Near-real-time analytics
- Multi-region cloud cache grid
- Shared cache across Kubernetes pods

## 🧾 Pros & Cons

### ✅ Pros

- Advanced caching + data grid features
- Java-native and well integrated with Spring
- Multiple access protocols (REST, HotRod, JDBC)
- Strong support for transactions and querying

### ⚠️ Cons

- More complex setup compared to Redis/Memcached
- Slightly heavier resource usage
- Learning curve for grid-specific features

## 📎 References

- [Infinispan Official Website](https://infinispan.org)
- [Infinispan Spring Boot Docs](https://infinispan.org/docs/stable/titles/spring/spring.html)
- [HotRod Protocol](https://infinispan.org/docs/stable/titles/hotrod/hotrod.html)

### Examples:

/_
Project: Spring Boot + Infinispan (Embedded Mode)
Description: A simple Spring Boot application using Infinispan for caching
_/

### 1. pom.xml

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
<modelVersion>4.0.0</modelVersion>
<groupId>com.example</groupId>
<artifactId>infinispan-demo</artifactId>
<version>1.0.0</version>
<packaging>jar</packaging>

    <properties>
        <java.version>21</java.version>
        <spring.boot.version>3.2.5</spring.boot.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-cache</artifactId>
        </dependency>

        <dependency>
            <groupId>org.infinispan</groupId>
            <artifactId>infinispan-spring-boot-starter-embedded</artifactId>
            <version>14.0.0.Final</version>
        </dependency>

        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
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

### 2. src/main/resources/application.properties

```properties
spring.cache.type=infinispan
```

### 3. src/main/java/com/example/demo/DemoApplication.java

```java
package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class DemoApplication {
public static void main(String[] args) {
SpringApplication.run(DemoApplication.class, args);
}
}
```

### 4. src/main/java/com/example/demo/model/Product.java

```java
package com.example.demo.model;

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

### 5. src/main/java/com/example/demo/service/ProductService.java

```java
package com.example.demo.service;

import com.example.demo.model.Product;
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
            throw new IllegalStateException(e);
        }
    }

}
```

### 6. src/main/java/com/example/demo/controller/ProductController.java

```java
package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.service.ProductService;
import org.springframework.web.bind.annotation.\*;

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

### 7. Run & Test

```txt
// Start the application and access: http://localhost:8080/products/1
// The first call takes ~3 seconds, subsequent calls are instant due to caching.
```
