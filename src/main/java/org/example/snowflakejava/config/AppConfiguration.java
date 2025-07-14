package org.example.snowflakejava.config;

import com.snowflake.snowpark_java.Session;
import com.zaxxer.hikari.HikariDataSource;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class AppConfiguration {

    @Bean
    public DataSource snowflakeDataSource(SnowflakeProperties snowflakeProperties) throws SQLException {

        String jdbcUrl = String.format(
                "jdbc:snowflake://%s.snowflakecomputing.com/?warehouse=%s&db=%s&schema=%s",
                snowflakeProperties.accountIdentifier(),
                snowflakeProperties.warehouse(),
                snowflakeProperties.db(),
                snowflakeProperties.schema()
        );

        DataSourceProperties dataSourceProperties = new DataSourceProperties();
        dataSourceProperties.setUsername(snowflakeProperties.username());
        dataSourceProperties.setPassword(snowflakeProperties.password());
        dataSourceProperties.setUrl(jdbcUrl);
        dataSourceProperties.setDriverClassName("net.snowflake.client.jdbc.SnowflakeDriver");

        HikariDataSource hikariObj = (HikariDataSource) dataSourceProperties.initializeDataSourceBuilder().build();
        hikariObj.setMaximumPoolSize(10);
        hikariObj.setConnectionTimeout(250);
        hikariObj.setMinimumIdle(5);
        hikariObj.setLoginTimeout(30000);
        hikariObj.setIdleTimeout(10000);
        return hikariObj;
    }

    @Bean
    public Session snowflakeSession(SnowflakeProperties snowflakeProperties) {
        Map<String, String> properties = new HashMap<>();
        properties.put("URL", "https://%s.snowflakecomputing.com:443".formatted(snowflakeProperties.accountIdentifier()));
        properties.put("USER", snowflakeProperties.username());
        properties.put("PASSWORD", snowflakeProperties.password());
        properties.put("WAREHOUSE", snowflakeProperties.warehouse());
        properties.put("DB", snowflakeProperties.db());
        properties.put("SCHEMA", snowflakeProperties.schema());
        return Session.builder().configs(properties).create();
    }

    /*
    Because we pass the datasource bean here, we don't have to specify jdbcUrl, username and password for liquibase plugin in pom xml
     */
    @Bean
    public SpringLiquibase liquibase(@Qualifier("snowflakeDataSource")
                                         DataSource snowflakeDataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(snowflakeDataSource);
        liquibase.setChangeLog("classpath:db/changelog/master.xml");
        return liquibase;
    }

    @Bean
    public JdbcTemplate snowflakeJdbcTemplate(@Qualifier("snowflakeDataSource")
                                                  DataSource snowflakeDataSource){
        return new JdbcTemplate(snowflakeDataSource);
    }
}
