# 📦 Kafka Integration — E-Commerce Modular Monolith

This project simulates a **Kafka-integrated modular monolith** for an e-commerce system. The main focus is not on building a complete shopping platform, but to **learn and implement Apache Kafka in a real-world, service-oriented architecture using Spring Boot.**

---

## 🎯 Purpose of the Project

> 🧠 This is a **learning-focused architecture project**, not a production-level e-commerce app.

- ✅ Understand **Kafka producer/consumer internals**
- ✅ Explore **inter-service communication** using **Kafka topics**
- ✅ Implement **event-driven** message flows
- ✅ Preserve **message ordering** using key-based partitioning
- ✅ Practice **modular monolithic service segregation**
- ✅ Reduce coupling between services through Kafka decoupling

---

## 🚀 Objective

- Enable **service-to-service** communication using **Kafka topics**
- Ensure **ordered processing** for each `orderId` using **key-based partitioning**
- Trigger **notifications**, update **order status**, and initiate **inventory/shipping** asynchronously
- Improve **scalability** and **fault tolerance**

---

## 🧩 Services in the Architecture

| Service         | Responsibility                          |
|-----------------|------------------------------------------|
| Order Service   | Creates/updates orders and status        |
| Payment Service | Handles payment lifecycle                |
| Inventory       | Updates and manages stock                |
| Shipping        | Triggers and updates delivery            |
| Notification    | Sends push, SMS, and email notifications |

---


## 🔄 Kafka Event Flow (Simplified)

```text
[Order Service]
   └── orders.created
   └── order.payment.initiated
   ◄── payment.success / failed / pending
   └── order.status.updated

[Payment Service]
   ◄── order.payment.initiated
   └── payment.success / failed / pending
   └── inventory.update
   └── shipping.initiate

[Shipping Service]
   ◄── shipping.initiate
   └── shipping.status

[Notification Service]
   ◄── orders.created
   ◄── order.status.updated
   ◄── payment.*
   ◄── shipping.status
   
```   

## 📌 Kafka Topics Overview

| Topic Name                         | Publisher         | Consumers             | Intent                                           |
|-----------------------------------|--------------------|------------------------|--------------------------------------------------|
| `ecommerce.orders.created`        | Order Service      | Notification Service   | Notify user of new order                         |
| `ecommerce.order.payment.initiated` | Order Service    | Payment Service        | Trigger payment processing                       |
| `ecommerce.payment.success`       | Payment Service    | Order, Notification    | Update order status and notify on success        |
| `ecommerce.payment.failed`        | Payment Service    | Order, Notification    | Update order status and notify on failure        |
| `ecommerce.payment.pending`       | Payment Service    | Order, Notification    | Notify user of pending status                    |
| `ecommerce.order.status.updated`  | Order Service      | Notification Service   | Notify user on status changes like SHIPPED       |
| `ecommerce.inventory.update`      | Payment Service    | Inventory Service      | Reserve or update stock after payment            |
| `ecommerce.shipping.initiate`     | Payment Service    | Shipping Service       | Initiate shipment after payment                  |
| `ecommerce.shipping.status`       | Shipping Service   | Notification Service   | Notify user of shipping updates                  |

---

## 🧩 Kafka Integration Per Service

### ✅ **Order Service**
- **Produces:**
    - `ecommerce.orders.created`
    - `ecommerce.order.payment.initiated`
    - `ecommerce.order.status.updated`
- **Consumes:**
    - `ecommerce.payment.success`
    - `ecommerce.payment.failed`
    - `ecommerce.payment.pending`
- **Responsibility:**
    - Manages order lifecycle and status
    - Updates DB and sends notifications on payment updates

---

### ✅ **Payment Service**
- **Consumes:**
    - `ecommerce.order.payment.initiated`
- **Produces:**
    - `ecommerce.payment.success`
    - `ecommerce.payment.failed`
    - `ecommerce.payment.pending`
    - `ecommerce.inventory.update`
    - `ecommerce.shipping.initiate`
- **Responsibility:**
    - Processes payment
    - Triggers downstream services after transaction

---

### ✅ **Inventory Service**
- **Consumes:**
    - `ecommerce.inventory.update`
- **Responsibility:**
    - Manages stock/reservation

---

### ✅ **Shipping Service**
- **Consumes:**
    - `ecommerce.shipping.initiate`
- **Produces:**
    - `ecommerce.shipping.status`
- **Responsibility:**
    - Ships orders and updates delivery status

---

### ✅ **Notification Service**
- **Consumes:**
    - `ecommerce.orders.created`
    - `ecommerce.order.status.updated`
    - `ecommerce.payment.success`
    - `ecommerce.payment.failed`
    - `ecommerce.payment.pending`
    - `ecommerce.shipping.status`
- **Responsibility:**
    - Sends SMS, Email, and Push notifications

---

## 🧠 Design Decisions

- All events related to an entity (`orderId`) are produced with that ID as the **Kafka message key**, ensuring **partition-based ordering**.
- Each topic has **3 partitions** and listener concurrency is set to 3 for parallelism.
- Every service uses a dedicated **consumer group** to consume messages.
- Messages are sent using **POJOs** and serialized with `JsonSerializer`.

---
