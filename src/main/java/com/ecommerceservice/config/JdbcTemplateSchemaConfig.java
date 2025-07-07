package com.ecommerceservice.config;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class JdbcTemplateSchemaConfig {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateSchemaConfig(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    public void setSearchPath() {
        jdbcTemplate.execute("SET search_path TO 'e_commerce'");
    }
}
