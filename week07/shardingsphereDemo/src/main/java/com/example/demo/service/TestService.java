package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


@Component
public class TestService {

    private DataSource dataSource;

    @Autowired
    public TestService(DataSource dataSource) throws SQLException {
        this.dataSource = dataSource;
        testDataSource();
    }


    public void testDataSource() throws SQLException {


        Connection conn = dataSource.getConnection();
        Statement statement = conn.createStatement();

        String sql = "select * from t1";
        statement.execute(sql);


        sql = "insert into t1 VALUES (2);";
        statement.execute(sql);


        sql = "select * from t1";
        statement.execute(sql);
    }

}
