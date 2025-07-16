package org.example.snowflakejava.config;

import org.hibernate.dialect.Dialect;

// If you want to use this dialect in AppConfiguration::entityManagerFactory
// hibernateProperties.setProperty("hibernate.dialect", "org.example.snowflakejava.config.SnowflakeDialect");
public final class SnowflakeDialect extends Dialect {}