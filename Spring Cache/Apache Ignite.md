# 🔥 Apache Ignite – Distributed Caching and In-Memory Computing Platform

## 🧠 What is Apache Ignite?

**Apache Ignite** is an **in-memory computing platform** that supports **distributed caching**, **data grids**, **SQL querying**, **ACID transactions**, and **compute grid** capabilities. It is designed to **store and process data in memory** across a cluster of machines, making it ideal for **high-performance**, **low-latency**, and **real-time** applications.

---

## 🚀 Key Features

| Feature               | Description                                                               |
| --------------------- | ------------------------------------------------------------------------- |
| Distributed Cache     | In-memory key-value store with optional disk persistence                  |
| SQL Queries           | ANSI-99 compliant SQL support across in-memory data                       |
| ACID Transactions     | Supports atomic operations across distributed nodes                       |
| Compute Grid          | Run compute tasks close to the data for high performance                  |
| Persistence           | Optional durable storage with write-through and read-through capabilities |
| Collocated Processing | Execute logic where data resides, reducing network latency                |
| Machine Learning      | Built-in ML algorithms on distributed data                                |
| Integration           | Spring, JDBC, JPA, Kafka, Hadoop, and more                                |

---

## ⚙️ Apache Ignite in Spring

Apache Ignite provides support for **Spring Cache Abstraction** via its **Spring Data Ignite** and **JCache** integrations.

### Maven Dependency

```xml
<dependency>
  <groupId>org.apache.ignite</groupId>
  <artifactId>ignite-spring</artifactId>
  <version>2.15.0</version>
</dependency>
```
