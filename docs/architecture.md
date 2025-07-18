# Project Architecture: DDD, Hexagonal, and Spring Integration

This document outlines the architectural principles applied in this `spring-integration-prototype` project, combining Domain-Driven Design (DDD) with Hexagonal Architecture (Ports and Adapters), and leveraging Spring Integration as a key enabling technology for adapters.

## 1. Core Principles

### 1.1. Domain-Driven Design (DDD)

DDD focuses on aligning the software design with the core business domain. Key concepts applied here include:

*   **Ubiquitous Language:** The terms used in the code (e.g., `Order`, `OrderPlacedEvent`) directly reflect the business language.
*   **Aggregates:** `Order` is treated as an aggregate root, ensuring consistency within its boundaries.
*   **Domain Events:** `OrderPlacedEvent` represents a significant occurrence in the domain, which other parts of the system can react to.

### 1.2. Hexagonal Architecture (Ports and Adapters)

This architectural style aims to create a highly decoupled application where the core business logic (the "hexagon") is isolated from external concerns (databases, UI, messaging systems).

*   **The Hexagon (Core Application):**
    *   **Domain Layer (`com.example.spring_integration_prototype.domain`):** Contains the pure business logic, entities, value objects, and domain events. It has no dependencies on external frameworks or infrastructure.
    *   **Application Layer (`com.example.spring_integration_prototype.application`):** Defines the application's use cases and orchestrates the domain objects. It contains:
        *   **Inbound Ports (Driving Ports):** Interfaces defining how the application can be driven (e.g., `PlaceOrderUseCase`). These represent the application's API for its use cases.
        *   **Outbound Ports (Driven Ports):** Interfaces defining what the application needs from external systems (e.g., `NotificationPort`). The application calls these ports, but doesn't know their concrete implementations.

*   **Adapters (Outside the Hexagon):**
    *   **Driving Adapters:** Implementations that drive the application by calling its Inbound Ports. Examples:
        *   `infrastructure.web.OrderController`: A REST controller that translates HTTP requests into calls to `PlaceOrderUseCase`.
    *   **Driven Adapters:** Implementations that fulfill the application's Outbound Ports. Examples:
        *   `infrastructure.integration.NotificationAdapter`: Implements `NotificationPort` and uses Spring Integration to send domain events to the messaging infrastructure.

## 2. Role of Spring Integration

Spring Integration is primarily used here to implement **Adapters** for the Hexagonal Architecture. It provides a powerful framework for connecting the application's core to external systems and for orchestrating complex message flows.

### 2.1. Inbound Adapters (Driving)

While not explicitly shown as a dedicated Spring Integration inbound adapter in this simplified example (the `OrderController` is a standard Spring MVC controller), Spring Integration could easily provide inbound adapters for:
*   Listening to Kafka topics for new orders.
*   Polling a directory for order files.
*   Receiving messages from an AMQP queue.

These would then translate the external message into a call to the `PlaceOrderUseCase` (an Inbound Port).

### 2.2. Outbound Adapters (Driven)

Spring Integration excels at implementing Driven Adapters. In this project:

*   **`infrastructure.integration.NotificationAdapter`:** This class implements the `NotificationPort`. When the `OrderService` (in the Application Layer) calls `notificationPort.notify()`, the `NotificationAdapter` uses a Spring Integration `@MessagingGateway` to send the `OrderPlacedEvent` to an internal Spring Integration channel (`orderPlacedChannel`). This completely decouples the `OrderService` from the actual notification mechanisms.

*   **`infrastructure.integration.IntegrationConfig`:** This configuration defines the internal Spring Integration flows that react to the `OrderPlacedEvent` published to `orderPlacedChannel`.
    *   `orderPlacedChannel`: A `publishSubscribe` channel. This is crucial for broadcasting the `OrderPlacedEvent` to multiple interested parties (e.g., email service, shipping service) without the publisher knowing about them.
    *   `emailNotificationFlow`: Subscribes to `orderPlacedChannel` and simulates sending an email. In a real application, this flow would contain an actual email outbound adapter.
    *   `shippingNotificationFlow`: Also subscribes to `orderPlacedChannel` and simulates notifying the shipping department. This flow would contain an adapter to the shipping system (e.g., a REST call, a message to a queue).

## 3. Project Structure and Flow

The project is structured to reflect these architectural layers:

```
src/main/java/com/example/spring_integration_prototype/
├── application/                  <-- Application Layer (Use Cases, In/Out Ports)
│   ├── port/
│   │   ├── in/                   <-- Inbound Ports (e.g., PlaceOrderUseCase)
│   │   └── out/                  <-- Outbound Ports (e.g., NotificationPort)
│   └── OrderService.java         <-- Use Case Implementation
├── domain/                       <-- Domain Layer (Core Business Logic)
│   └── model/
│       ├── Order.java            <-- Aggregate Root
│       └── OrderPlacedEvent.java <-- Domain Event
├── infrastructure/               <-- Infrastructure Layer (Adapters)
│   ├── integration/              <-- Spring Integration Driven Adapters
│   │   ├── NotificationAdapter.java
│   │   └── IntegrationConfig.java
│   └── web/                      <-- Web Driving Adapters
│       └── OrderController.java
└── SpringIntegrationPrototypeApplication.java
```

**Flow of an Order Placement:**

1.  **External System (e.g., Browser/Client):** Sends an HTTP POST request to `/orders`.
2.  **`OrderController` (Driving Adapter):** Receives the HTTP request, parses it, and calls `placeOrderUseCase.placeOrder(...)`.
3.  **`OrderService` (Application Layer):**
    *   Creates an `Order` domain object.
    *   Performs any core business logic related to placing the order.
    *   Calls `notificationPort.notify(new OrderPlacedEvent(...))`.
4.  **`NotificationAdapter` (Driven Adapter):**
    *   Receives the `OrderPlacedEvent` from `OrderService`.
    *   Uses its internal `NotificationGateway` (a Spring Integration gateway) to send the `OrderPlacedEvent` to the `orderPlacedChannel`.
5.  **`orderPlacedChannel` (Spring Integration Publish-Subscribe Channel):** Broadcasts the `OrderPlacedEvent` to all its subscribers.
6.  **`emailNotificationFlow` (Spring Integration Flow - Driven Adapter):** Receives the `OrderPlacedEvent` and simulates sending an email.
7.  **`shippingNotificationFlow` (Spring Integration Flow - Driven Adapter):** Receives the `OrderPlacedEvent` and simulates notifying the shipping department.

This architecture ensures that changes to external systems (e.g., switching from email to SMS, or changing shipping providers) only affect the relevant adapters in the `infrastructure` layer, leaving the core `domain` and `application` layers untouched. Spring Integration provides the flexible plumbing to make these connections.
