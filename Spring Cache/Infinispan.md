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
