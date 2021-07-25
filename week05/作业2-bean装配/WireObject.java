package com.htx.schoolstarter.auto;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.stereotype.Component;

@Component(value = "byAnnoataion")
public class WireObject implements BeanNameAware {

    String name;

    public void hi() {
        System.out.println("hi "+ name);
    }

    @Override
    public void setBeanName(String s) {
        name = s;
    }
}
