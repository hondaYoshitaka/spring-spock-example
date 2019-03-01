package com.github.hondaYoshitaka.configuration;

import com.zaxxer.hikari.HikariDataSource;
import org.dbunit.DataSourceDatabaseTester;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import javax.sql.DataSource;

/**
 * Spring/3rd Partyライブラリ間のトランザクションを繋ぐためのDataSourceを提供するconfigurationクラス.
 * ----------------------------------------------------------------------
 * - for DbUnit
 *
 * @author hondaYoshitaka
 * @see <a href="https://qiita.com/kazuki43zoo/items/d9182c8ddb2b754f150f">3rdパーティ製のDBアクセスライブラリをSpringのトランザクション管理下に参加させる方法</a>
 */
@Configuration
public class DataSourceConfiguration {

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
