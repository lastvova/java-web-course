package com.bobocode.demo;

import com.bobocode.demo.context.ApplicationContext;
import com.bobocode.demo.context.ApplicationContextImpl;
import com.bobocode.demo.service.GreetingService;
import com.bobocode.demo.service.HelloService;
import lombok.SneakyThrows;

public class DemoWebApp {

    public static void main(String[] args) {
        var context = new ApplicationContextImpl("com.bobocode.demo");

        var hello = context.getBean(HelloService.class);
        var helloByName = context.getBean("helloService", GreetingService.class);
        var whatsUp = context.getBean("someBean",GreetingService.class);

        var allBeans = context.getAllBeans(GreetingService.class);
        GreetingService someBean = allBeans.get("someBean");

        hello.saySomething("there");
        helloByName.saySomething("By name there");
        whatsUp.saySomething("dude");
        someBean.saySomething("All");
    }
}
