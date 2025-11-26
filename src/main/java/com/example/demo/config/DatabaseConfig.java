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
        // First, try JDBC_DATABASE_URL (already in JDBC format)
        String jdbcUrl = System.getenv("JDBC_DATABASE_URL");
        
        if (jdbcUrl != null && !jdbcUrl.isEmpty()) {
            // JDBC_DATABASE_URL is already in the correct format with credentials
            // Format: jdbc:postgresql://user:password@host:port/database
            String[] parts = jdbcUrl.split("@");
            if (parts.length == 2) {
                String credentials = parts[0].replace("jdbc:postgresql://", "");
                String hostAndDb = parts[1];
                
                String[] credParts = credentials.split(":");
                String username = credParts[0];
                String password = credParts.length > 1 ? credParts[1] : "";
                
                String url = "jdbc:postgresql://" + hostAndDb;
                
                return DataSourceBuilder.create()
                        .url(url)
                        .username(username)
                        .password(password)
                        .build();
            }
        }
        
        // Fallback: try DATABASE_URL (Render's default format)
        String databaseUrl = System.getenv("DATABASE_URL");
        
        if (databaseUrl != null && databaseUrl.startsWith("postgres://")) {
            // Convert Render's postgres:// URL to jdbc:postgresql://
            databaseUrl = databaseUrl.replace("postgres://", "jdbc:postgresql://");
            
            // Parse the URL to extract components
            String[] parts = databaseUrl.split("@");
            if (parts.length == 2) {
                String credentials = parts[0].replace("jdbc:postgresql://", "");
                String hostAndDb = parts[1];
                
                String[] credParts = credentials.split(":");
                String username = credParts[0];
                String password = credParts.length > 1 ? credParts[1] : "";
                
                String url = "jdbc:postgresql://" + hostAndDb;
                
                return DataSourceBuilder.create()
                        .url(url)
                        .username(username)
                        .password(password)
                        .build();
            }
        }
        
        // Fallback to default Spring Boot configuration
        return DataSourceBuilder.create().build();
    }
}
