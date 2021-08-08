package com.javacourse.week07.configuration;

import com.javacourse.week07.routing.DataSourceRouter;
import com.javacourse.week07.routing.DatabaseEnum;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class MyConfig {


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

    @Bean(name = "dataSourceRouter")
    public DataSource clientDatasource() {
        Map<Object, Object> targetDataSources = new HashMap<>();
        DataSource masterDatasource = masterDataSource();
        DataSource slaveBDatasource = slaveDataSource();
        targetDataSources.put(DatabaseEnum.DATASOURCE_MASTER.getValue(),
                masterDatasource);
        targetDataSources.put(DatabaseEnum.DATASOURCE_SLAVE.getValue(),
                slaveBDatasource);

        DataSourceRouter dataSourceRouter
                = new DataSourceRouter();
        dataSourceRouter.setTargetDataSources(targetDataSources);
        dataSourceRouter.setDefaultTargetDataSource(masterDatasource);
        return dataSourceRouter;
    }



}
