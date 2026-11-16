package com.quiz.collectionservice.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dsql.DsqlUtilities;

public class DsqlDataSourceConfig {
    private static Connection jdbConnection=null;
    static long startTime=System.currentTimeMillis();

    private static final String REGION = System.getenv("REGION");
    private static final DsqlUtilities utilities = DsqlUtilities.builder()
            .region(Region.of(REGION.toLowerCase()))
            .credentialsProvider(DefaultCredentialsProvider.create()).build();
    private static final String AURORA_DSQL_CLUSTER_ENDPOINT = System.getenv("AURORA_DSQL_CLUSTER_ENDPOINT");
    private static final String JDBC_URL = "jdbc:postgresql:" + AURORA_DSQL_CLUSTER_ENDPOINT + ":5432/postgres?sslmode=require";

    private static HikariDataSource hds;
    static {
        System.out.println("region: " + REGION);
        System.out.println("url: " + AURORA_DSQL_CLUSTER_ENDPOINT);
        final HikariConfig config = new HikariConfig();
        config.setUsername("username");
        config.setJdbcUrl(JDBC_URL);
        config.setMaxLifetime(1500 * 1000);
        config.setMaximumPoolSize(1);

        String authToken = getAuthTokenForAdminUser();

        config.setPassword(authToken);
    }

    public static Connection getPooledConnection() throws SQLException {
        // Use generateDbConnectAuthToken when connecting as `admin` user
        long startTime=System.currentTimeMillis();
        String authToken = getAuthTokenForAdminUser();
        hds.setPassword(authToken);
        Connection connection= hds.getConnection();
        long endTime=System.currentTimeMillis();
        System.out.println("time to create hikari pooled connection in ms "+(endTime-startTime));
        return connection;

    }

    public static String getAuthTokenForAdminUser() {
        long startTimeAuthToken = System.currentTimeMillis();
        String authToken = utilities
                .generateDbConnectAdminAuthToken(builder -> builder.hostname(AURORA_DSQL_CLUSTER_ENDPOINT)
                        .region(Region.of(REGION))
                        .expiresIn(Duration.ofSeconds(900)));
        System.out.println(authToken);
        long endTimeAuthToken = System.currentTimeMillis();
        System.out.println("time to create auth token: " + (endTimeAuthToken - startTimeAuthToken));

        return authToken;
    }
}
