package com.lqx.service;

import org.junit.Test;

import static org.junit.Assert.*;

public class SxtApplicationContextTest {

    @Test
    public void getBean() {
        ApplicationContext context = new SxtApplicationContext("spring.xml");
        HelloService helloService = (HelloService) context.getBean("helloService");
        helloService.hello();
    }
}