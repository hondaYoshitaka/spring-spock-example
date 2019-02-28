package com.github.hondaYoshitaka.configuration;

import com.zaxxer.hikari.HikariDataSource;
import org.dbunit.DataSourceDatabaseTester;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    public DataSource dataSource(final DataSourceProperties properties) {
        final DataSource dataSource = properties.initializeDataSourceBuilder()
                                                .type(HikariDataSource.class)
                                                .build();
        return new TransactionAwareDataSourceProxy(dataSource);
    }

    @Bean
    public DataSourceDatabaseTester dataSourceDatabaseTester(final DataSource dataSource) {
        return new DataSourceDatabaseTester(dataSource);
    }
}
