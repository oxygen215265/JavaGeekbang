package com.javacourse.week07.repository;

import com.javacourse.week07.aop.DbType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.SQLException;

@Repository
public class OrderRepository {

    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public OrderRepository(@Qualifier(value = "dataSourceRouter") DataSource dataSource,JdbcTemplate jdbcTemplate) {
        this.dataSource = dataSource;
        this.jdbcTemplate = jdbcTemplate;
    }

    @DbType(value = "master")
    public void showMasterConnnection() throws SQLException {
        System.out.println(jdbcTemplate.getDataSource().getConnection().getMetaData().getURL());
    }

    @DbType(value = "slave")
    public void showSlaveConnnection() throws SQLException {
        System.out.println(jdbcTemplate.getDataSource().getConnection().getMetaData().getURL());
    }






}
