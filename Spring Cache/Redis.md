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
