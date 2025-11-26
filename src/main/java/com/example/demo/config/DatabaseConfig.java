package com.example.demo.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    @Bean
    @Primary
    public DataSource dataSource() {
        String databaseUrl = System.getenv("DATABASE_URL");
        
        if (databaseUrl != null && databaseUrl.startsWith("postgres://")) {
            // Convert Render's postgres:// URL to jdbc:postgresql://
            databaseUrl = databaseUrl.replace("postgres://", "jdbc:postgresql://");
            
            // Parse the URL to extract components
            // Format: jdbc:postgresql://user:password@host:port/database
            String[] parts = databaseUrl.split("@");
            if (parts.length == 2) {
                String credentials = parts[0].replace("jdbc:postgresql://", "");
                String hostAndDb = parts[1];
                
                String[] credParts = credentials.split(":");
                String username = credParts[0];
                String password = credParts.length > 1 ? credParts[1] : "";
                
                String jdbcUrl = "jdbc:postgresql://" + hostAndDb;
                
                return DataSourceBuilder.create()
                        .url(jdbcUrl)
                        .username(username)
                        .password(password)
                        .build();
            }
        }
        
        // Fallback to default Spring Boot configuration
        return DataSourceBuilder.create().build();
    }
}
