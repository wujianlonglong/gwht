package com.longer.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;


/**
 * Created by wujianlong on 2017/5/27.
 */
@Configuration
public class DataSourceConfig {

    @Bean(name = "sjowDataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    @Primary
    public DataSource sjowDataSource() {
        return DataSourceBuilder.create().build();
    }

}
