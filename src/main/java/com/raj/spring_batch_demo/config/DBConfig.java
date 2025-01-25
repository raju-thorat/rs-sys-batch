package com.raj.spring_batch_demo.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
public class DBConfig {

    @Bean
    @ConfigurationProperties("spring.datasource.rs-sys-ds")
    public DataSourceProperties rsSysDatasourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource rsSysDataSource() {
        return rsSysDatasourceProperties()
                .initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean
    public PlatformTransactionManager rsSysTransactionManager() {
        return new DataSourceTransactionManager(rsSysDataSource());
    }

}