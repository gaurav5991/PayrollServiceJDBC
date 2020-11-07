package com.bridgelabz.employeepayroll;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

public class DemoDB {
    public static void main(String[] args) {
        String jdbcURL = "jdbc:mysql://localhost:3306/payroll_service";
        String userName = "root";
        String password = "G@ur@v@123";
        Connection connection;
        System.out.println("Welcome to Employee Payroll Database System");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver Loaded");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Cannot find the driver in the classpath!", e);
        }
        listDrivers();
        try {
            connection = DriverManager.getConnection(jdbcURL, userName, password);
            System.out.println("Connection Successful: " + connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * Method to get List of Drivers
     */
    private static void listDrivers() {
        Enumeration<Driver> driversList = DriverManager.getDrivers();
        while (driversList.hasMoreElements()) {
            Driver driverClass = driversList.nextElement();
            System.out.println("Driver Name: " + driverClass.getClass().getName());
        }
    }
}
