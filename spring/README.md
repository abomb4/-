# 学 spring
什么是Spring框架？Spring框架有哪些主要模块？
什么是控制反转(IOC)？什么是依赖注入？
请解释下Spring框架中的IoC？
BeanFactory和ApplicationContext有什么区别？
请解释Spring Bean的生命周期？
Spring框架中有哪些不同类型的事件？

25、Spring 框架中都用到了哪些设计模式？

Spring框架中使用到了大量的设计模式，下面列举了比较有代表性的：

    代理模式—在AOP和remoting中被用的比较多。
    单例模式—在spring配置文件中定义的bean默认为单例模式。
    模板方法—用来解决代码重复的问题。比如. RestTemplate, JmsTemplate, JpaTemplate。
    前端控制器—Spring提供了DispatcherServlet来对请求进行分发。
    视图帮助(View Helper )—Spring提供了一系列的JSP标签，高效宏来辅助将分散的代码整合在视图里。
    依赖注入—贯穿于BeanFactory / ApplicationContext接口的核心理念。
    工厂模式—BeanFactory用来创建对象的实例。
