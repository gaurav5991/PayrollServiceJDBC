package com.bridgelabz.employeepayroll;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeePayrollFileDBService {

    /*Method to read data from  database using JDBC*/
    public List<EmployeePayroll> readData() {
        String sql = "Select * from employee_payroll; ";
        List<EmployeePayroll> employeePayrollList = new ArrayList<>();
        try(Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                double salary = resultSet.getDouble("salary");
                LocalDate startDate= resultSet.getDate("start").toLocalDate();
                EmployeePayroll employeePayroll = new EmployeePayroll(id, name, salary, startDate);
                employeePayrollList.add(employeePayroll);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeePayrollList;
    }

    /*Method to setup connection using getConnection method*/
    private Connection getConnection() throws SQLException {
        String jdbcURL = "jdbc:mysql://localhost:3306/payroll_service?useSSL=false";
        String userName = "root";
        String password = "Gaurav@123";
        Connection con;
        System.out.println("Connecting to database: "+jdbcURL);
        con = DriverManager.getConnection(jdbcURL, userName, password);
        System.out.println("Connection Successful: " + con);
        return con;
    }
}
