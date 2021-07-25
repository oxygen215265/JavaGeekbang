package com.htx.schoolstarter.jdbc;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HikariExample {

    public void SampleHikariDemo() throws SQLException {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/demo1");
        config.setUsername("root");
        config.setPassword("");
        config.setMaximumPoolSize(5);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        HikariDataSource ds = new HikariDataSource(config);
        Connection connection = ds.getConnection();

        String selectQuery = "select * from users where id=?";
        PreparedStatement statement2 = connection.prepareCall(selectQuery);
        statement2.setInt(1,1);
        ResultSet rs = statement2.executeQuery();
        while(rs.next()) {
            System.out.println(rs.getString("name"));
        }


    }

}
