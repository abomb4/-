package com.abomb4.learn.java.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 啥是代理？有几种代理？咋创建代理？
 * <p>
 * 代理是一种设计模式，提供对目标对象的访问的封装，即通过代理对象访问目标对象。
 * 基于此模式，可以实现不修改原始类的情况下，对目标对象功能进行各类扩展。
 * <p>
 * 在 Java 中，一般分为静态代理与动态代理两种主要模式。
 * <p>
 * 静态代理一般是，代理类与目标类实现相同的接口，代理类的实例包装目标类的对象，实现功能的扩展。
 * 动态代理是在运行时为某对象创建代理对象。
 * <p>
 * 本示例创建静态代理、JDK 动态代理、CGLIB 公台代理。
 *
 * @author abomb4 2020-01-16
 */
public class Main {

    /**
     * 驱动
     *
     * @param args 无用
     */
    public static void main(String[] args) {
        // 原始对象
        final TheInterface originObject = new TheInterfaceImpl();

        // 1. 静态代理
        final TheInterface staticProxy;
        staticProxy = new TheStaticProxyOfTheInterface(originObject);

        // 2. JDK 动态代理
        final TheInterface jdkDynamicProxy;
        jdkDynamicProxy = (TheInterface) Proxy.newProxyInstance(
                TheInvocationHandler.class.getClassLoader(),
                new Class[]{TheInterface.class}, new TheInvocationHandler());


        // 3. CGLIB 代理
        final TheInterface cglibDynamicProxy;
    }

    /** 1. 静态代理 **/
    static class TheStaticProxyOfTheInterface implements TheInterface {

        final TheInterface origin;

        TheStaticProxyOfTheInterface(final TheInterface origin) {
            this.origin = origin;
        }

        @Override
        public String hello(final String name) {
            final String hello = origin.hello(name);
            return "这条 Hello [" + hello + "] 已经被污染啦！";
        }
    }

    /** 2. jdk 动态代理 **/
    static class TheInvocationHandler implements InvocationHandler {

        @Override
        public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
            log("更加屌的污染！");
            final Object invoke = method.invoke(proxy, args);
            return "[" + invoke + "]污染至极！你都不知道哪里把你改了！";
        }
    }

    private static void log(String msg) {
        System.out.println(msg);
    }
}
