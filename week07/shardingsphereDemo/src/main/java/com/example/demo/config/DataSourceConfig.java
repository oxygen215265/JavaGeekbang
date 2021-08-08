package com.example.demo.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.shardingsphere.driver.api.ShardingSphereDataSourceFactory;
import org.apache.shardingsphere.readwritesplitting.api.ReadwriteSplittingRuleConfiguration;
import org.apache.shardingsphere.readwritesplitting.api.rule.ReadwriteSplittingDataSourceRuleConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;

@Configuration
public class DataSourceConfig {

    public DataSource masterDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3316/db?serverTimezone=UTC");
        config.setUsername("root");
        config.setPassword("");
        config.setMaximumPoolSize(2);
        config.addDataSourceProperty("cachePrepStmts", "true");
        return new HikariDataSource(config);

    }


    public DataSource slaveDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3326/db?serverTimezone=UTC");
        config.setUsername("root");
        config.setPassword("");
        config.setMaximumPoolSize(2);
        config.addDataSourceProperty("cachePrepStmts", "true");
        return new HikariDataSource(config);

    }

    @Bean
    public DataSource shardingSphereDataSource() throws SQLException {

        Map<String, DataSource> dataSourceMap = new HashMap<>();
        DataSource masterDatasource = masterDataSource();
        DataSource slaveDatasource = slaveDataSource();
        dataSourceMap.put("ds1",masterDatasource);
        dataSourceMap.put("ds2",slaveDatasource);

        Properties properties = new Properties();
        properties.setProperty("sql.show", "true");


        ReadwriteSplittingDataSourceRuleConfiguration dataSourceRuleConfiguration = new ReadwriteSplittingDataSourceRuleConfiguration(
    "master_slave_db", null, "ds1", Arrays.asList("ds2"), "ROUND_ROBIN");

        ReadwriteSplittingRuleConfiguration readwriteSplittingRuleConfiguration = new ReadwriteSplittingRuleConfiguration(Collections.singleton(dataSourceRuleConfiguration), new HashMap<>());

        return ShardingSphereDataSourceFactory.createDataSource(dataSourceMap, Collections.singleton(readwriteSplittingRuleConfiguration), properties);

    }


}
