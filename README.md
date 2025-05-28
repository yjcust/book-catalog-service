#  Book Catalog Service

一个基于 Spring Boot 构建的图书管理系统，支持图书的增删查改
---

## ✅ 项目亮点

### 1. 架构设计
- 使用 **Controller-Service-Mapper-Model** 分层架构。
- `BookDTO` 用于参数接收，`Book` 为持久化实体。
- 使用 `MyBatis-Plus` 简化数据库操作。

### 2. 设计模式合理应用

#### 🏭 工厂模式（Factory Pattern）
- 通过 `BookFactory.createBook(BookDTO)` 将 DTO 转换为实体对象，实现对象创建逻辑与业务逻辑解耦。
- 使用场景：当输入格式可能变化时，集中处理转换逻辑更便于维护。

#### 👀 观察者模式（Observer Pattern）
- `BookUpdateNotifier` 作为主题类，注册了如 `LogObserver` 等观察者，在更新图书时统一通知。
- 使用场景：当图书更新可能触发多种行为（如日志记录、通知系统等），可扩展性强。


### 3. 统一的异常处理机制
- 自定义异常类 `BusinessException`。
- 枚举类 `ErrorCode` 管理错误码和错误信息。
- 全局异常处理类 `GlobalExceptionHandler` 统一包装系统异常和业务异常返回结果。
- 返回结构统一为 `BaseResponse<T>` 格式。

### 4. 单元测试覆盖核心逻辑
- 使用 JUnit5 + Mockito 编写单元测试。
- 针对参数校验、更新逻辑、异常分支做了重点测试。