package org.example.snowflakejava;

import org.example.snowflakejava.config.SnowflakeProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({ SnowflakeProperties.class })
public class SnowflakeJavaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SnowflakeJavaApplication.class, args);
    }

}
