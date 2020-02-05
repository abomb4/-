# 学 spring
- 什么是Spring框架？Spring框架有哪些主要模块？
    - 按 Spring 框架官网的介绍：
    > Spring 框架为基于 Java 开发的企业应用提供了完整的变成与配置模型。
    > Spring 的关键功能是在应用级别提供架构支持：Spring专注于企业应用程序的“管道”，
    > 使开发团队专注于业务逻辑的开发，与特定的部署环境没有关联。
    
    - Spring 框架的主要功能：
    > - 核心功能: 依赖注入, 事件, 资源管理, 国际化, 校验, 数据绑定, 类型转换, 脚本 SpEL, 切片 AOP.
    > - 测试功能：mock 对象, TestContext framework, Spring MVC Test, WebTestClient.
    > - 数据访问：事务框架, DAO support, JDBC, ORM, 对象XML映射 Marshalling XML.
    > - web 框架：Spring MVC、 Spring WebFlux.
    > - 技术集成: remoting, JMS, JCA, JMX, email, tasks, scheduling, cache.
    > - 开发语言：Kotlin, Groovy, dynamic languages.

    - Spring 的主要模块？
        - 核心模块：beans、core、context、expression
        - 测试：test
        - 数据访问：jdbc, orm, oxm, jms, tx
        - Web：web, webflux, webmvc, websocket
        - AOP：aop, aspects

- 什么是控制反转(IOC)？什么是依赖注入？

    答：“控制反转（IOC）”与“依赖注入（DI）”说明了同一种东西。
    IOC 是一种“原则”，Spring 的实现要求对象定义其依赖时，只能定义在：
    - 构造函数的参数
    - 工厂方法的参数
    - 由工厂创建并返回的对象实例的属性上
    
    遵循此标准，IOC 容器会在创建 bean 的时候进行依赖注入。
    
    这种模式是把对象自己找依赖的过程反转过来，不再由对象自行寻找而是由容器进行注入，称为“控制反转”。
    
    参考（https://docs.spring.io/spring/docs/5.2.3.RELEASE/spring-framework-reference/core.html#beans-introduction）

- 请解释下Spring框架中的IoC？
    
    按官方说法，`org.springframework.beans` 和 `org.springframework.context` 两个包组成 IoC 容器的基础。
    `BeanFactory` 接口提供了先进的机制用于管理任何类型的对象。
    `ApplicationContext` 接口是 `BeanFactory` 的子接口，添加了一些功能：
    - AOP 集成
    - 国际化
    - 事件发布
    - 应用层 context，例如 `WebApplicationContect`
    

- BeanFactory和ApplicationContext有什么区别？

- 请解释Spring Bean的生命周期？

- Spring框架中有哪些不同类型的事件？

- Spring 框架中都用到了哪些设计模式？

    Spring框架中使用到了大量的设计模式，下面列举了比较有代表性的：
    
        代理模式—在AOP和remoting中被用的比较多。
        单例模式—在spring配置文件中定义的bean默认为单例模式。
        模板方法—用来解决代码重复的问题。比如. RestTemplate, JmsTemplate, JpaTemplate。
        前端控制器—Spring提供了DispatcherServlet来对请求进行分发。
        视图帮助(View Helper )—Spring提供了一系列的JSP标签，高效宏来辅助将分散的代码整合在视图里。
        依赖注入—贯穿于BeanFactory / ApplicationContext接口的核心理念。
        工厂模式—BeanFactory用来创建对象的实例。
