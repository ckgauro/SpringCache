# 🌩 Hazelcast — In-Memory Data Grid (IMDG)

## 🧠 What is Hazelcast?

**Hazelcast** is an open-source, in-memory data grid (IMDG) that provides distributed data structures, caching, and computing capabilities for Java applications.

It allows you to **store and share data across multiple nodes** in a cluster, making it ideal for distributed caching, session replication, real-time stream processing, and high-availability systems.

---

## 🚀 Key Features

| Feature                  | Description                                                          |
| ------------------------ | -------------------------------------------------------------------- |
| Distributed Caching      | Share cache across cluster nodes for scalability and fault tolerance |
| JCache (JSR-107) Support | Compliant with Java caching standard                                 |
| Data Structures          | Maps, sets, queues, lists, multi-maps, topics                        |
| Discovery Mechanisms     | Supports Kubernetes, Docker, Eureka, Zookeeper                       |
| Management Center        | GUI dashboard for monitoring and managing clusters                   |
| Spring Boot Integration  | First-class support for Spring Boot and Spring Cache                 |
| High Availability        | Partitioning, replication, and automatic failover                    |

---

## ⚙️ Spring Boot Integration

### 📦 Dependency

```xml
<dependency>
  <groupId>com.hazelcast</groupId>
  <artifactId>hazelcast-spring</artifactId>
</dependency>
```

## 🗺 Deployment Options

- **Standalone (embedded)** — Runs within your Spring Boot application JVM.
- **Client-server mode** — Connects your application to an external Hazelcast cluster.
- **Cloud-native** — Deployable and scalable on Kubernetes, GCP, AWS, or Azure.

---

## 📊 When to Use Hazelcast

| Use Case                            | Why Hazelcast?                                   |
| ----------------------------------- | ------------------------------------------------ |
| Distributed cache for microservices | In-memory data grid with auto-partitioning       |
| Session replication                 | Cluster-wide state sharing for HTTP sessions     |
| Real-time analytics                 | Fast, in-memory SQL querying and data processing |
| Distributed locks & coordination    | Built-in semaphores, atomic variables, and locks |
| Caching + compute                   | Supports distributed execution and compute tasks |

# 🌩 Hazelcast + Spring Boot 3.4.2 + JDK 21 — Complete Caching Example

This is a full working example of using **Hazelcast** with **Spring Boot 3.4.2** and **JDK 21** to implement distributed caching using Spring's caching abstraction.

---

## 📦 Tech Stack

- Java 21
- Spring Boot 3.4.2
- Hazelcast 5.x
- Spring Cache Abstraction

---

## 📁 Project Structure

```
├── src/
│ ├── main/
│ │ ├── java/com/example/demo/
│ │ │ ├── DemoApplication.java
│ │ │ ├── config/HazelcastConfig.java
│ │ │ ├── model/Product.java
│ │ │ ├── service/ProductService.java
│ │ │ └── controller/ProductController.java
│ └── resources/
│ └── application.yml
└── pom.xml

```

---

## 🧩 `pom.xml`

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" ...>
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.example</groupId>
  <artifactId>hazelcast-spring-demo</artifactId>
  <version>1.0</version>
  <properties>
    <java.version>21</java.version>
    <spring.boot.version>3.4.2</spring.boot.version>
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
      <groupId>com.hazelcast</groupId>
      <artifactId>hazelcast-spring</artifactId>
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-actuator</artifactId>
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

### ⚙️ `HazelcastConfig.java`

```java
package com.example.demo.config;

import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.cache.CacheManager;
import org.springframework.cache.hazelcast.HazelcastCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HazelcastConfig {

    @Bean
    public Config hazelcastConfig() {
        Config config = new Config();
        config.setInstanceName("hazelcast-instance");

        MapConfig mapConfig = new MapConfig("products");
        mapConfig.setTimeToLiveSeconds(300);
        mapConfig.setEvictionConfig(new EvictionConfig()
            .setEvictionPolicy(EvictionPolicy.LRU)
            .setMaxSizePolicy(MaxSizePolicy.FREE_HEAP_SIZE));

        config.addMapConfig(mapConfig);
        return config;
    }

    @Bean
    public HazelcastInstance hazelcastInstance() {
        return Hazelcast.newHazelcastInstance(hazelcastConfig());
    }

    @Bean
    public CacheManager cacheManager(HazelcastInstance hazelcastInstance) {
        return new HazelcastCacheManager(hazelcastInstance);
    }

}
```

### 💾 `Product.java`

```java
package com.example.demo.model;

public record Product(Long id, String name, double price) {}
💡 ProductService.java
java
Copy
Edit
package com.example.demo.service;

import com.example.demo.model.Product;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Cacheable(value = "products", key = "#id")
    public Product getProductById(Long id) {
        simulateSlowService();
        return new Product(id, "Product-" + id, id * 10.5);
    }

    private void simulateSlowService() {
        try {
            Thread.sleep(3000); // Simulate slow response
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

}
```

### 🌐 `ProductController.java`

```java
package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.service.ProductService;
import org.springframework.web.bind.annotation.\*;

@RestController
@RequestMapping("/api/products")
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

### 📄 application.yml

```yaml
spring:
  application:
    name: hazelcast-demo
  cache:
    type: hazelcast
  main:
    banner-mode: off
management:
  endpoints:
    web:
      exposure:
        include: "*"
logging:
  level:
    org.springframework.cache: DEBUG
```

### ▶️ Run & Test

```bash
curl http://localhost:8080/api/products/1
# First call ~3 seconds
# Second call ~0.001 seconds (cached)
```

### ✅ Output Example

```nginx
INFO  Cache miss for key '1' on cache 'products'
INFO  Cache hit for key '1' on cache 'products'
```

## 🧪 What You Learned

- How to configure Hazelcast for Spring caching
- How `@Cacheable` stores results in a distributed cache
- Hazelcast TTL and eviction behavior
- Java 21 and Spring Boot 3.4.2 compatibility
