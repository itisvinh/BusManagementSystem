package com.busmanagementsystem.Database.Configs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static Connection connection;

    static {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException ex) {
            System.out.println(ex);
        }
    }

    private DBConnection() {
    }

    public static Connection getConn() throws SQLException {
        if (connection == null) {
            //vinh
            String connectionString = "jdbc:sqlserver://localhost;instanceName=SQLEXPRESS;databaseName=OUBus;integratedSecurity=true";
            // khoi
            //String connectionString = "jdbc:sqlserver://localhost;instanceName=SQLEXPRESS;databaseName=OUBus;user= ;password=";
            connection = DriverManager.getConnection(connectionString);
        }
        return connection;
    }

}
