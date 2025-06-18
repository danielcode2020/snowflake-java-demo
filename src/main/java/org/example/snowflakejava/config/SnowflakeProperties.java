package org.example.snowflakejava.config;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "snowflake", ignoreUnknownFields = false)
public record SnowflakeProperties(
        String username,
        String password,
        String accountIdentifier,
        String warehouse,
        String db,
        String schema
) { }
