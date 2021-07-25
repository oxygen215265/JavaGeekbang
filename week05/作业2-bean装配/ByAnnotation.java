package com.htx.schoolstarter.auto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.naming.Name;

@Component(value="byAnnotation")
public class ByAnnotation {

    @Autowired
    public ByAnnotation(WireObject wireObject) {
        wireObject.hi();
    }

}
