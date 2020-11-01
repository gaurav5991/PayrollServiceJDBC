package com.bridgelabz.employeepayroll;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeePayrollFileDBService {

    /**
     * Created Singleton Objects
     */
    private static EmployeePayrollFileDBService employeePayrollFileDBService;
    private PreparedStatement employeePayrollDataStatement;

    /**
     * Created Private Constructor so singleton objects can't be accessed from outside the class
     */
    private EmployeePayrollFileDBService() {
    }

    /**
     * To get Instance of singleton Objects
     *
     * @return
     */
    public static EmployeePayrollFileDBService getInstance() {
        if (employeePayrollFileDBService == null)
            employeePayrollFileDBService = new EmployeePayrollFileDBService();
        return employeePayrollFileDBService;
    }

    /*Method to read data from  database using JDBC*/
    public List<EmployeePayroll> readData() throws EmployeePayrollException {
        String sql = "Select * from employee_payroll; ";
        List<EmployeePayroll> employeePayrollList = new ArrayList<>();
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            employeePayrollList = this.getEmployeepayrollData(resultSet);
        } catch (SQLException e) {
            throw new EmployeePayrollException(e.getMessage(), EmployeePayrollException.ExceptionType.UNABLE_TO_CONNECT);
        }
        return employeePayrollList;
    }

    /*Method to setup connection using getConnection method*/
    private Connection getConnection() throws SQLException {
        String jdbcURL = "jdbc:mysql://localhost:3306/payroll_service?useSSL=false";
        String userName = "root";
        String password = "Gaurav@123";
        Connection con;
        System.out.println("Connecting to database: " + jdbcURL);
        con = DriverManager.getConnection(jdbcURL, userName, password);
        System.out.println("Connection Successful: " + con);
        return con;
    }

    /**
     * Overloaded getEmployeePayrollData Method
     *
     * @param name
     * @return List
     */
    public List<EmployeePayroll> getEmployeepayrollData(String name) throws EmployeePayrollException {
        List<EmployeePayroll> employeePayrollList = null;
        if (this.employeePayrollDataStatement == null)
            this.prepareStatementForEmployeeData();
        try {
            employeePayrollDataStatement.setString(1, name);
            ResultSet resultSet = employeePayrollDataStatement.executeQuery();
            employeePayrollList = this.getEmployeepayrollData(resultSet);
        } catch (SQLException e) {
            throw new EmployeePayrollException(e.getMessage(), EmployeePayrollException.ExceptionType.PROBLEM_IN_PREPARED_STATEMENT);
        }
        return employeePayrollList;
    }

    /**
     * '
     * Overloaded getEmployeePayrollData Method
     *
     * @param resultSet
     * @return
     */
    private List<EmployeePayroll> getEmployeepayrollData(ResultSet resultSet) throws EmployeePayrollException {
        List<EmployeePayroll> employeePayrollList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                double salary = resultSet.getDouble("salary");
                LocalDate startDate = resultSet.getDate("start").toLocalDate();
                employeePayrollList.add(new EmployeePayroll(id, name, salary, startDate));
            }
        } catch (SQLException e) {
            throw new EmployeePayrollException(e.getMessage(), EmployeePayrollException.ExceptionType.PROBLEM_IN_RESULTSET);
        }
        return employeePayrollList;
    }

    /**
     * Method to create prepared statement
     */
    private void prepareStatementForEmployeeData() throws EmployeePayrollException {
        try {
            Connection connection = this.getConnection();
            String sql = "Select * from employee_payroll where name  = ?";
            employeePayrollDataStatement = connection.prepareStatement(sql);
        } catch (SQLException e) {
            throw new EmployeePayrollException(e.getMessage(), EmployeePayrollException.ExceptionType.UNABLE_TO_CONNECT);
        }
    }

    /**
     * Method to Update employee data
     *
     * @param name
     * @param salary
     * @return
     */
    public int updateEmployeeData(String name, double salary) throws EmployeePayrollException {
        return this.updateEmployeeDataUsingPreparedStatement(name, salary);
    }

    private int updateEmployeeDataUsingPreparedStatement(String name, double salary) throws EmployeePayrollException {
        try (Connection connection = this.getConnection()) {
            String sql = "update employee_payroll set salary = ? where name  = ? ";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setDouble(1, salary);
            preparedStatement.setString(2, name);
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new EmployeePayrollException(e.getMessage(), EmployeePayrollException.ExceptionType.UNABLE_TO_CONNECT);
        }
    }
}
