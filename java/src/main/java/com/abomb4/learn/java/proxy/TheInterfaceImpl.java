package com.abomb4.learn.java.proxy;

import lombok.extern.slf4j.Slf4j;

/**
 * @author abomb4 2020-01-16
 */
@Slf4j
public class TheInterfaceImpl implements TheInterface {

    @Override
    public String hello(final String name) {
        log.debug("原始类：您好，[{}]", name);
        return "您好, [" + name + "]";
    }
}
