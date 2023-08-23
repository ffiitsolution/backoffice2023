/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ffi.api.backoffice.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;


/**
 *
 * @author IT
 */
public class ConnectionUtils {
    private static final String DB_DRIVER = "oracle.jdbc.driver.OracleDriver";
    private static final String DB_URL = "jdbc:oracle:thin:@192.168.10.28:1521:orcl";
    private static final String DB_USERNAME = "HQ";
    private static final String DB_PASSWORD = "kfc14022";

    public static Connection getConnection() {
        Connection conn = null;
        try {
            //Register the JDBC driver
            Class.forName(DB_DRIVER);

            //Open the connection
            conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            if (conn != null) {
                System.out.println("Successfully connected.");
            } else {
                System.out.println("Failed to connect.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
}
