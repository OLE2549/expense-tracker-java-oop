
package com.finance.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DBConnection {

    private static final String URL = env("DB_URL",
            "jdbc:mysql://localhost:3306/expense_tracker?useSSL=false&serverTimezone=Asia/Bangkok&characterEncoding=UTF-8");

    private static final String USER = env("DB_USER", "root");
    private static final String PASSWORD = env("DB_PASSWORD", "");

    private DBConnection() {
    }

    private static String env(String key, String fallback) {
        String value = System.getenv(key);
        return value == null || value.isBlank() ? fallback : value;
    }

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("ไม่พบ MySQL JDBC Driver ในเว็บแอป", e);
        }

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
