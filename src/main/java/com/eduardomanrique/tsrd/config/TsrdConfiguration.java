package com.eduardomanrique.tsrd.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

/**
 * Created by emanrique on 18/06/17.
 */
@Configuration
@Slf4j
public class TsrdConfiguration {

    @Bean
    public JdbcTemplate getJdbcTemplate() {

        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        EmbeddedDatabase db = builder
                .setType(EmbeddedDatabaseType.DERBY)
                .addScript("sql/create-db.sql")
                .addScript("sql/insert-data.sql")
                .build();
        return new JdbcTemplate(db);
    }
}