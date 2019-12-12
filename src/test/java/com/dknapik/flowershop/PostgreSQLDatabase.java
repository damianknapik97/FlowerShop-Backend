package com.dknapik.flowershop;

import org.testcontainers.containers.PostgreSQLContainer;

/**
 * Singleton that creates docker container with PostgreSQL Database inside
 */
public class PostgreSQLDatabase extends PostgreSQLContainer<PostgreSQLDatabase> {
    private static final String IMAGE = "postgres:11.1";
    private static PostgreSQLContainer instance;

    private PostgreSQLDatabase() {
        super(IMAGE);
    }

    public static PostgreSQLContainer getInstance() {
        if (instance == null) {
            instance = new PostgreSQLContainer();
        }
        return instance;
    }

    /**
     * Run container with database, and set system properties allowing for connection to database
     */
    @Override
    public void start() {
        super.start();
        System.setProperty("DB_URL", instance.getJdbcUrl());
        System.setProperty("DB_USERNAME", instance.getUsername());
        System.setProperty("DB_PASSWORD", instance.getPassword());
    }

    /**
     * Handled by JVM
     */
    @Override
    public void stop(){ }
}
