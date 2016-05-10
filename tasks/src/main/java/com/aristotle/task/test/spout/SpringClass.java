package com.aristotle.task.test.spout;

import org.springframework.stereotype.Component;

@Component
public class SpringClass {

    public void printData(String data) {
        System.out.println("From Spring Bean " + data);
    }
}
