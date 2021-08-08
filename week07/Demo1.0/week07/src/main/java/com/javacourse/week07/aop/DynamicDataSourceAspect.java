package com.javacourse.week07.aop;

import com.javacourse.week07.routing.ClientDatabaseContextHolder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class DynamicDataSourceAspect {


    @Pointcut("@annotation(com.javacourse.week07.aop.DbType)")
    public void pointCut() {};

    @Before(value="pointCut() && @annotation(db)",argNames = "joinPoint,db")
    public void before(JoinPoint joinPoint, DbType db) {
        String dbName = db.value();
        ClientDatabaseContextHolder.set(dbName);
    }



}
