package com.javacourse.week07.aop;


import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented

public @interface DbType {
    String value() default "slave";
}
