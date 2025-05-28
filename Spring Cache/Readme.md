# 📦 What is Caching?

**Caching** is a technique used in software development to **store copies of frequently accessed data in temporary storage** (a "cache") so that future requests for that data can be served faster.

Instead of querying a slow data source (like a database or external API) repeatedly, applications use cache to improve **performance, responsiveness**, and **scalability**.

---

## 🚀 Benefits of Caching in Software Development

| Benefit                   | Description                                                                 |
| ------------------------- | --------------------------------------------------------------------------- |
| ⚡ Faster Performance     | Reduces latency by avoiding repeated computation or network/database calls. |
| 📉 Reduces Load           | Minimizes load on backend systems like databases or web services.           |
| 💸 Cost-Effective         | Reduces compute time and network usage, which can lower operational costs.  |
| 🧩 Improved Scalability   | Handles high traffic without proportionally increasing backend load.        |
| 🕒 Better User Experience | Faster response times enhance user satisfaction.                            |

---

# 🧠 Types of Caching in Spring Framework

Spring Framework supports various caching strategies and types to improve performance by reducing unnecessary method calls and data access. This guide covers the types of caching available within the Spring caching abstraction.

---

## 📚 Table of Contents

1. [🗂 In-Memory Caching](#1-in-memory-caching)
2. [🌐 Distributed Caching](#2-distributed-caching)
3. [🧾 Declarative Caching](#3-declarative-caching)
4. [🧬 Programmatic Caching](#4-programmatic-caching)
5. [📊 Level-Based Caching](#5-level-based-caching)
6. [⛓ Synchronized Caching (Spring 4.3+)](#6-synchronized-caching-spring-43)
7. [⚛️ Reactive Caching (Spring 5+)](#7-reactive-caching-spring-5)
8. [🛠 Custom Caching](#8-custom-caching)

---

## 1. In-Memory Caching

🗂 Caches data locally within the application's memory. Fast access but not shared across multiple instances.

**Examples:**

- `ConcurrentMapCache` (Spring default)
- `Caffeine`
- `EhCache`

```java
@Cacheable("products")
public Product getProductById(Long id) {
    // Simulates slow service
    return new Product(id, "Sample");
}
```

## 2. Distributed Caching

🌐 Caches data in an **external shared system**, enabling consistent cache access across multiple application instances.

### ✅ Examples

- **Redis**
- **Hazelcast**
- **Memcached**
- **Apache Ignite**
- **Infinispan**

### ⚙️ Example Configuration (`application.properties`)

```properties
spring.cache.type=redis
```

## 3. Declarative Caching

🧾 Uses annotations to **declaratively define caching behavior** directly on methods.

### 🧩 Key Annotations

| Annotation    | Description                                  |
| ------------- | -------------------------------------------- |
| `@Cacheable`  | Stores method result in cache if not present |
| `@CachePut`   | Always executes method and updates cache     |
| `@CacheEvict` | Removes specified entries from the cache     |
| `@Caching`    | Combines multiple caching annotations        |

---

## 4. Programmatic Caching

🧬 Provides **manual control** over caching using `CacheManager` and `Cache` interfaces — useful for dynamic or conditional scenarios.

### 💻 Example

```java
@Autowired
private CacheManager cacheManager;

public void updateCacheManually(String key, Product product) {
    Cache cache = cacheManager.getCache("products");
    if (cache != null) {
        cache.put(key, product);
    }
}
```

## 5. Level-Based Caching

📊 Implements a **multi-level caching strategy** to optimize both speed and consistency in distributed systems.

### 🔹 Types

- **L1 (Level 1)**: Local in-memory cache (e.g., **Caffeine**)
- **L2 (Level 2)**: Distributed/shared cache (e.g., **Redis**)

### ✅ Benefit

- **L1** provides **ultra-fast access** with no network overhead.
- **L2** ensures **data consistency** and **availability across instances**.
- Ideal for microservices and horizontally scaled applications.

---

## 6. Synchronized Caching (Spring 4.3+)

⛓ Prevents a **cache stampede** by **synchronizing concurrent access** when the cache entry is missing.

### 🔐 Example

```java
@Cacheable(value = "users", key = "#id", sync = true)
public User getUser(int id) {
    // Simulated DB call
    return userRepository.findById(id).orElse(null);
}
```

## 7. Reactive Caching (Spring 5+)

⚛️ Designed for **non-blocking**, **asynchronous** applications using **Spring WebFlux**.

### 🔧 Tools

- **Spring WebFlux**
- **Reactive Redis clients** (e.g., **Lettuce**, **Redisson**)

### 🔁 Example Use Case

Use `Mono` or `Flux` to **cache** and **return** values within a reactive stream:

```java
public Mono<User> getUserReactive(int id) {
    return reactiveRedisTemplate.opsForValue()
        .get("user:" + id)
        .switchIfEmpty(
            Mono.fromCallable(() -> userRepository.findById(id).orElse(null))
                .doOnNext(user ->
                    reactiveRedisTemplate.opsForValue()
                        .set("user:" + id, user)
                        .subscribe()
                )
        );
}
```

## 8. Custom Caching

🛠 Define your own caching strategy using Spring’s `CacheManager` for advanced or hybrid scenarios.

### 🧱 Example

```java
@Bean
public CacheManager cacheManager() {
    SimpleCacheManager manager = new SimpleCacheManager();
    manager.setCaches(List.of(new ConcurrentMapCache("custom")));
    return manager;
}

```

- Can integrate with **custom key generation**, **conditional caching**, and **eviction strategies**.

- Suitable for applications with **complex business rules** or **fallback mechanisms**.

---

## 📌 Summary

| Type         | Scope            | Use Case                                      |
| ------------ | ---------------- | --------------------------------------------- |
| In-Memory    | Local            | Fast access, single instance                  |
| Distributed  | External/shared  | Scalability, multi-instance setup             |
| Declarative  | Annotation-based | Simple and common caching patterns            |
| Programmatic | Manual control   | Complex conditional logic                     |
| Level-Based  | Multi-layer      | Combines speed (L1) with scalability (L2)     |
| Synchronized | Thread-safe init | Avoid cache stampede with synchronized access |
| Reactive     | Non-blocking     | Reactive systems using Spring WebFlux         |
| Custom       | Fully customized | Specialized logic, fallback, or hybrid models |
