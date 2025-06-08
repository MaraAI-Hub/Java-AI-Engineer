[toc]

# 什么是 Fluent API
**Fluent API** 是一种设计模式，通过**链式方法调用**提供一种流畅、可读性强的编程接口。它旨在使代码更接近自然语言，提高代码的可读性、可维护性与表达力。
在 Java 中，Fluent API 并非语法特性，而是一种**风格化的 API 设计方式**。它通常使用方法链（method chaining）来表达逻辑流程，被广泛应用于多个框架中，例如：
* **Java 8中的流表达式**
* **Hibernate / JPA**（ORM 构建条件查询）
* **Spring WebClient / Spring Data JPA等**
* **Mockito**（测试 DSL）
* **Lombok**（生成 Builder）
* **自定义配置器或查询构建器等场景**
---

# Fluent API 的核心思想

Fluent API 的核心在于：**每个方法返回对象本身（通常是 `this`），从而支持连续调用多个方法**，构成链式操作。

## 1. 自定义 Fluent API 示例

下面是一个简单的用户查询构建器：

```java
public class UserQueryBuilder {
    private String name;
    private Integer minAge;
    private Integer maxAge;

    public UserQueryBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public UserQueryBuilder withMinAge(int minAge) {
        this.minAge = minAge;
        return this;
    }

    public UserQueryBuilder withMaxAge(int maxAge) {
        this.maxAge = maxAge;
        return this;
    }

    public List<User> execute() {
        // 伪代码：构建查询并返回用户列表
        return new ArrayList<>();
    }
}
```

使用方式：

```java
List<User> users = new UserQueryBuilder()
    .withName("John")
    .withMinAge(18)
    .withMaxAge(30)
    .execute();
```

这种方式清晰表达了查询逻辑，非常易读。

---

# Fluent API 在 Spring 中的应用
在 Spring 生态中，Fluent API 被广泛应用于各类框架和组件中，凭借其链式调用的设计，使得配置与调用逻辑更直观、易读。无论是构建异步 HTTP 请求的 WebClient，编写动态查询条件的 Spring Data JPA Specification，还是配置安全规则的 Spring Security DSL，Fluent API 都以其流畅的语法风格提升了开发效率和代码表达力，是现代 Spring 编程风格中不可或缺的一部分。
## Spring WebClient 

`WebClient` 是 Spring 5 引入的响应式 HTTP 客户端，采用了 Fluent API 风格，支持非阻塞、异步 HTTP 通信。它在响应式应用中被广泛使用，特别适合与 `Mono` / `Flux` 搭配使用。

---
### WebClient Fluent API 示例

```java
WebClient webClient = WebClient.create();

Mono<String> result = webClient
    .get()
    .uri("https://api.example.com/data")
    .header("Authorization", "Bearer token")
    .retrieve()
    .bodyToMono(String.class);
```

### 分析：

| 步骤                 | 功能说明               |
| ------------------ | ------------------ |
| `.get()`           | 设置 HTTP 方法为 GET    |
| `.uri(...)`        | 设置请求地址             |
| `.header(...)`     | 添加请求头              |
| `.retrieve()`      | 发起请求并准备处理响应        |
| `.bodyToMono(...)` | 将响应内容映射为目标类型（JSON） |

---

## 更复杂的 Fluent API 使用场景

### 1. 带 Token 的 POST 请求：

```java
webClient.post()
    .uri("https://api.example.com/users")
    .header("Authorization", "Bearer token")
    .contentType(MediaType.APPLICATION_JSON)
    .bodyValue(new User("Alice", 30))
    .retrieve()
    .bodyToMono(Response.class);
```

### 2. 异步链式：先获取 token，再 POST 数据

```java
Mono<String> tokenMono = webClient.get()
    .uri("https://api.example.com/auth")
    .retrieve()
    .bodyToMono(TokenResponse.class)
    .map(TokenResponse::getAccessToken);

tokenMono.flatMap(token ->
    webClient.post()
        .uri("https://api.example.com/data")
        .header("Authorization", "Bearer " + token)
        .bodyValue(new Payload("Hello", 123))
        .retrieve()
        .bodyToMono(ServerResponse.class)
).subscribe(
    resp -> System.out.println("Response: " + resp),
    err -> System.err.println("Error: " + err.getMessage())
);
```

---

## Fluent API 的特性总结

| 特性     | 描述                          |
| ------ | --------------------------- |
| 方法链    | 连续返回构建器对象，可连续调用方法           |
| 可组合    | 和 `Mono` / `Flux` 等响应式流无缝组合 |
| DSL 风格 | 类似自然语言，语义清晰                 |
| 灵活配置   | 支持函数式 URI 构造、动态请求头、异常处理等    |

---

# Fluent API 的优点

✅ Fluent API 的主要优势包括：

* **可读性强**：链式结构清晰表达业务流程
* **配置灵活**：支持多种参数传递方式
* **函数式表达力强**：与 Java 8+ Lambda 结合自然
* **响应式兼容性好**：可与 Reactor/异步机制配合使用
* **易于扩展**：构建自定义 DSL 非常便利

---

# Fluent API 的缺点与局限

虽然 Fluent API 十分流行，但它也存在一些天然的缺陷，特别是在继承、线程安全和调试方面。

---

## 缺点 1：不利于继承扩展

如果父类方法返回 `this`，子类无法保证链式调用后仍是子类类型，导致断链。必须借助 Java 泛型技巧（如 CRTP）解决。

### 示例：

```java
class Animal<T extends Animal<T>> {
    public T setName(String name) {
        return (T) this;
    }
}

class Dog extends Animal<Dog> {
    public Dog setBreed(String breed) {
        return this;
    }
}

Dog d = new Dog()
    .setName("Buddy")
    .setBreed("Labrador");
```

> 泛型写法较复杂，对初学者不友好。

---

## 缺点 2：线程不安全

Fluent API 多依赖可变状态对象，在并发环境下容易出现竞态条件。

### 示例：

```java
ConfigBuilder builder = new ConfigBuilder();

Thread t1 = new Thread(() -> builder.setHost("localhost"));
Thread t2 = new Thread(() -> builder.setPort(8080));

t1.start(); t2.start();
System.out.println(builder.build()); // 可能是未定义行为
```

> 若不加锁或使用不可变对象，容易出错。

---

## 缺点 3：调试困难

链式调用使得错误堆栈定位困难，尤其是响应式调用（如 WebClient）中 `.onStatus()`、`.map()` 等链较长的场景，调试代价高。

---

# WebClient Fluent API 的底层实现原理

### 1. 分阶段构建接口

WebClient 内部使用**阶段式接口设计**（如 `RequestHeadersSpec`, `RequestBodySpec` 等）控制调用顺序。

### 2. 方法链设计

每个方法返回自身或下一个阶段接口，如：

```java
public interface RequestBodySpec {
    RequestBodySpec header(String name, String value);
    <T> Mono<T> bodyToMono(Class<T> clazz);
}
```

### 3. 响应式底层：Reactor

WebClient 最终返回的是 `Mono<T>` 或 `Flux<T>`，通过它们继续链式 `.map()`、`.flatMap()` 等响应式操作。

### 4. 不可变对象设计

虽然链式调用看似“修改同一个对象”，其实每一步通常返回**新构造的不可变副本**，从而实现线程安全。

---


延展：
与响应式编程以及流式API的关系。主要是随着Java8的lambda表达式。才开始。

