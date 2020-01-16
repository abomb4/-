package com.abomb4.learn.java.proxy;

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
     * @param args 无用
     */
    public static void main(String[] args) {
        // 原始对象
        final TheInterface originObject = new TheInterfaceImpl();

        // 1. 静态代理
        {

        }
    }
}
