package com.javacourse.week07.service;

import com.javacourse.week07.aop.DbType;
import com.javacourse.week07.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
public class TestService {

    private OrderRepository orderRepository;

    @Autowired
    public TestService(OrderRepository orderRepository) throws SQLException {
        this.orderRepository = orderRepository;
        testMaster();
        testSlave();
    }


    public void testMaster() throws SQLException {
        this.orderRepository.showMasterConnnection();
    }

    public void testSlave() throws SQLException {
        this.orderRepository.showSlaveConnnection();
    }




}
