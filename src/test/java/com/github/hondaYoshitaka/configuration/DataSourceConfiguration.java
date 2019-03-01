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

    /**
     * https://qiita.com/kazuki43zoo/items/d9182c8ddb2b754f150f#spring%E3%81%AE%E3%83%88%E3%83%A9%E3%83%B3%E3%82%B6%E3%82%AF%E3%82%B7%E3%83%A7%E3%83%B3%E7%AE%A1%E7%90%86%E4%B8%8B%E3%81%B8%E3%81%AE%E5%8F%82%E5%8A%A0
     *
     * @param properties
     * @return
     */
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
