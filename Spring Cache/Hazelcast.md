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
