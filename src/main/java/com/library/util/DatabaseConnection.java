package com.library.util;

import org.apache.commons.dbcp2.BasicDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Database connection utility class using connection pooling
 * Implements Singleton pattern and manages JDBC connections
 */
public class DatabaseConnection {
    private static DatabaseConnection instance;
    private BasicDataSource dataSource;
    
    private DatabaseConnection() {
        initializeDataSource();
    }
    
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }
    
    private void initializeDataSource() {
        Properties properties = new Properties();
        
        try (InputStream inputStream = getClass().getClassLoader()
                .getResourceAsStream("database.properties")) {
            
            if (inputStream == null) {
                throw new RuntimeException("database.properties file not found");
            }
            
            properties.load(inputStream);
            
            dataSource = new BasicDataSource();
            dataSource.setDriverClassName(properties.getProperty("db.driver"));
            dataSource.setUrl(properties.getProperty("db.url"));
            dataSource.setUsername(properties.getProperty("db.username"));
            dataSource.setPassword(properties.getProperty("db.password"));
            
            // Connection pool settings
            dataSource.setInitialSize(Integer.parseInt(
                    properties.getProperty("db.initialSize", "5")));
            dataSource.setMaxTotal(Integer.parseInt(
                    properties.getProperty("db.maxActive", "20")));
            dataSource.setMaxIdle(Integer.parseInt(
                    properties.getProperty("db.maxIdle", "10")));
            dataSource.setMinIdle(Integer.parseInt(
                    properties.getProperty("db.minIdle", "5")));
            
            // Connection validation
            dataSource.setValidationQuery("SELECT 1");
            dataSource.setTestOnBorrow(true);
            dataSource.setTestWhileIdle(true);
            
        } catch (IOException e) {
            throw new RuntimeException("Error loading database properties", e);
        }
    }
    
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
    
    public void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
    
    public void close() {
        if (dataSource != null) {
            try {
                dataSource.close();
            } catch (SQLException e) {
                System.err.println("Error closing data source: " + e.getMessage());
            }
        }
    }
}

