package com.bridgelabz.employeepayroll;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeePayrollFileDBService {

    /**
     * Created Singleton Objects
     */
    private static int connectionCounter = 0;
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
        String sql = "Select * from employee; ";
        return this.getEmployeepayrollUsingDB(sql);
    }

    /*Method to setup connection using getConnection method*/
    private static synchronized Connection getConnection() throws SQLException {
        connectionCounter++;
        String jdbcURL = "jdbc:mysql://localhost:3306/payroll_service";
        String userName = "root";
        String password = "G@ur@v@123";
        Connection con;
        System.out.println("Processing Thread: "+Thread.currentThread().getName()+"Connecting to the database with the Id:"+connectionCounter);
        con = DriverManager.getConnection(jdbcURL, userName, password);
        System.out.println("Processing Thread: "+Thread.currentThread().getName()+ "Id:"+connectionCounter+
                "connection is successful!!!!" + con);
        return con;
    }

    /**
     * Overloaded getEmployeePayrollData Method
     *
     * @param name
     * @return List
     */
    public List<EmployeePayroll> getEmployeepayrollData(String name) throws EmployeePayrollException {
        List<EmployeePayroll> employeePayrollList;
        if (this.employeePayrollDataStatement == null)
            this.prepareStatementForEmployeeData();
        try {
            employeePayrollDataStatement.setString(1, name);
            ResultSet resultSet = employeePayrollDataStatement.executeQuery();
            employeePayrollList = this.getEmployeepayrollData(resultSet);
        } catch (SQLException e) {
            throw new EmployeePayrollException(e.getMessage(), EmployeePayrollException.ExceptionType.SQLEXCEPTION);
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
                int id = resultSet.getInt("emp_id");
                String name = resultSet.getString("name");
                String phone = resultSet.getString("phone");
                String address = resultSet.getString("address");
                String gender = resultSet.getString("gender");
                LocalDate startDate = resultSet.getDate("start").toLocalDate();
                Double salary = resultSet.getDouble("salary");
                employeePayrollList.add(new EmployeePayroll(id, name, phone, address, gender, salary, startDate));
            }
        } catch (SQLException e) {
            throw new EmployeePayrollException(e.getMessage(), EmployeePayrollException.ExceptionType.PROBLEM_IN_RESULTSET);
        }
        return employeePayrollList;
    }

    /**
     * Method to get Employee data within given Date Range
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public List<EmployeePayroll> getEmployeepayrollForDateRange(LocalDate startDate, LocalDate endDate) throws EmployeePayrollException {
        String sql = String.format("Select * from employee where start between '%s' and '%s';", Date.valueOf(startDate), Date.valueOf(endDate));
        return this.getEmployeepayrollUsingDB(sql);
    }

    /**
     * @param sql
     * @return
     * @throws EmployeePayrollException
     */
    private List<EmployeePayroll> getEmployeepayrollUsingDB(String sql) throws EmployeePayrollException {
        List<EmployeePayroll> employeePayrollList;
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            employeePayrollList = this.getEmployeepayrollData(resultSet);
        } catch (SQLException e) {
            throw new EmployeePayrollException(e.getMessage(), EmployeePayrollException.ExceptionType.UNABLE_TO_CONNECT);
        }
        return employeePayrollList;
    }

    /**
     * Method to create prepared statement
     */
    private void prepareStatementForEmployeeData() throws EmployeePayrollException {
        try {
            Connection connection = this.getConnection();
            String sql = "Select * from employee where name  = ?";
            employeePayrollDataStatement = connection.prepareStatement(sql);
        } catch (SQLException e) {
            throw new EmployeePayrollException(e.getMessage(), EmployeePayrollException.ExceptionType.SQLEXCEPTION);
        }
    }

    /**
     * Method to Call UpdateEmployeeData Using Prepared Statement
     *
     * @param name
     * @param salary
     * @return
     */
    public int updateEmployeeData(String name, double salary) throws EmployeePayrollException {
        return this.updateEmployeeDataUsingPreparedStatement(name, salary);
    }

    /**
     * Method to update Employee details Using Satatement
     *
     * @param name
     * @param salary
     * @return
     * @throws EmployeePayrollException
     */
    private int updateEmployeeDataUsingStatement(String name, double salary) throws EmployeePayrollException {
        String sql = String.format("update employee set salary = %.2f where name = '%s';", name, salary);
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            return statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new EmployeePayrollException(e.getMessage(), EmployeePayrollException.ExceptionType.SQLEXCEPTION);
        }
    }

    /**
     * @param name
     * @param salary
     * @return
     * @throws EmployeePayrollException
     */
    private int updateEmployeeDataUsingPreparedStatement(String name, double salary) throws EmployeePayrollException {
        try (Connection connection = this.getConnection()) {
            String sql = "update employee set salary = ? where name  = ? ";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setDouble(1, salary);
            preparedStatement.setString(2, name);
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new EmployeePayrollException(e.getMessage(), EmployeePayrollException.ExceptionType.UNABLE_TO_CONNECT);
        }
    }

    /**
     * Method to calculate average salary of employee and adding it to Map
     *
     * @return
     * @throws EmployeePayrollException
     */
    public Map<String, Double> getAverageSalaryByGender() {
        String sql = "select gender,avg(salary) as avg_salary from employee group by gender";
        return getAggregateByGender("gender", "avg_salary", sql);
    }

    public Map<String, Double> getAggregateByGender(String gender, String aggregate, String sql) {
        Map<String, Double> genderCountMap = new HashMap<>();
        try (Connection connection = this.getConnection();) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String getgender = resultSet.getString(gender);
                Double count = resultSet.getDouble(aggregate);
                genderCountMap.put(getgender, count);
            }
        } catch (SQLException e) {
            e.getMessage();
        }
        return genderCountMap;
    }

    public Map<String, Double> getCountByGender() {
        String sql = "select gender,count(salary) as count_gender from employee group by gender";
        return getAggregateByGender("gender", "count_gender", sql);
    }

    public Map<String, Double> getMinimumByGender() {
        String sql = "select gender,min(salary) as minSalary_gender from employee group by gender";
        return getAggregateByGender("gender", "minSalary_gender", sql);
    }

    public Map<String, Double> getMaximumByGender() {
        String sql = "select gender,max(salary) as maxSalary_gender from employee group by gender";
        return getAggregateByGender("gender", "maxSalary_gender", sql);
    }

    public Map<String, Double> getSalarySumByGender() {
        String sql = "select gender,sum(salary) as sumSalary_gender from employee group by gender";
        return getAggregateByGender("gender", "sumSalary_gender", sql);
    }

    /**
     * Method to add Employee to Database using statement
     *
     * @param name
     * @param salary
     * @param startDate
     * @param gender
     * @return
     * @throws EmployeePayrollException
     */
    public EmployeePayroll addEmployeeToPayroll(String name, String gender, LocalDate startDate, double salary) throws EmployeePayrollException {
        int employeeId = -1;
        Connection connection = null;
        EmployeePayroll employeePayroll;
        try {
            connection = this.getConnection();
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new EmployeePayrollException(e.getMessage(), EmployeePayrollException.ExceptionType.SQLEXCEPTION);
        }
        try (Statement statement = connection.createStatement()) {
            String sql = String.format("insert into employee(id, name, gender,salary,start)" +
                    "values(%d,'%s','%s',%.2f,'%s'); ", name, gender, salary, Date.valueOf(startDate));
            int rowAffected = statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            if (rowAffected == 1) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    employeeId = generatedKeys.getInt("emp_id");
                }
            }
            employeePayroll = new EmployeePayroll(name, gender,salary, startDate);
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                throw new EmployeePayrollException(e1.getMessage(), EmployeePayrollException.ExceptionType.SQLEXCEPTION);
            }
            throw new EmployeePayrollException(e.getMessage(), EmployeePayrollException.ExceptionType.SQLEXCEPTION);
        }
        try (Statement statement = connection.createStatement()) {
            double deductions = salary * 0.2;
            double taxablePay = salary - deductions;
            double tax = taxablePay * 0.1;
            double netPay = salary - tax;
            String sql = String.format("INSERT INTO payroll(basic_pay,deductions,taxable_pay,tax,net_pay) VALUES "
                    + "(%.2f,%.2f,%.2f,%.2f,%.2f);",  salary, deductions, taxablePay, tax, netPay);
            int rowAffected = statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            if (rowAffected == 1) {
                ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next()) {
                    employeeId = resultSet.getInt("emp_id");
                }
            }
        } catch (SQLException e) {
            throw new EmployeePayrollException(e.getMessage(), EmployeePayrollException.ExceptionType.SQLEXCEPTION);
        }
        try {
            connection.commit();
        } catch (SQLException e) {
            throw new EmployeePayrollException(e.getMessage(), EmployeePayrollException.ExceptionType.SQLEXCEPTION);
        }
        return employeePayroll;
    }

    public EmployeePayroll addEmployeeToPayroll(int id, String name, String phone, String address, String gender, LocalDate startDate, double salary, int departmentId, String departmentName) throws EmployeePayrollException {
        int employeeId = -1;
        EmployeePayroll employeePayroll = null;
        Connection connection = null;
        try {
            connection = this.getConnection();
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new EmployeePayrollException(e.getMessage(), EmployeePayrollException.ExceptionType.SQLEXCEPTION);
        }
        try (Statement statement = connection.createStatement()) {
            String sql = String.format("insert into employee(emp_id, name,phone,address, gender,salary, start)" +
                    "values(%d,'%s','%s','%s','%s',%.2f,'%s'); ", id, name, phone, address, gender, salary, Date.valueOf(startDate));
            int rowAffected = statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            if (rowAffected == 1) {
                ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next()) {
                    employeeId = resultSet.getInt("emp_id");
                }
            }
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                throw new EmployeePayrollException(e1.getMessage(), EmployeePayrollException.ExceptionType.SQLEXCEPTION);
            }
            throw new EmployeePayrollException(e.getMessage(), EmployeePayrollException.ExceptionType.SQLEXCEPTION);
        }
        try (Statement statement = connection.createStatement()) {
            double deductions = salary * 0.2;
            double taxablePay = salary - deductions;
            double tax = taxablePay * 0.1;
            double netPay = salary - tax;
            String sql = String.format("INSERT INTO payroll(emp_id,basic_pay,deductions,taxable_pay,tax,net_pay) VALUES "
                    + "(%d,%.2f,%.2f,%.2f,%.2f,%.2f);", id, salary, deductions, taxablePay, tax, netPay);
            int rowAffected = statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            if (rowAffected == 1) {
                ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next()) {
                    employeeId = resultSet.getInt("emp_id");
                }
            }
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
            throw new EmployeePayrollException(e.getMessage(), EmployeePayrollException.ExceptionType.SQLEXCEPTION);
        }
        try (Statement statement = connection.createStatement()) {
            String sql = String.format("insert into department (emp_id,dept_id,dept_name) " + "VALUES (%d,%d,'%s')",
                    id, departmentId, departmentName);
            int rowAffected = statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            if (rowAffected == 1) {
                employeePayroll = new EmployeePayroll(id, name, phone, address, gender, salary, startDate);
            }
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
            throw new EmployeePayrollException(EmployeePayrollException.ExceptionType.SQLEXCEPTION);
        }
        try {
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return employeePayroll;
    }

    public void deleteEmployee(String name) throws EmployeePayrollException {
        String sql = String.format("update employee set is_active = 'false' where name = '%s';", name);
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new EmployeePayrollException(EmployeePayrollException.ExceptionType.SQLEXCEPTION);
        }
    }
    private int addToEmployeeTable(Connection connection, String name, String phone, String address, String gender, LocalDate startDate) {
        int employeeID = -1;
        String sqlQuery = String.format("insert into employee (name, gender, start, address, phone_number) " +
                "values ( '%s', '%s', '%s', '%s','%s');", name, gender, Date.valueOf(startDate), address, phone);
        try (Statement statement = connection.createStatement();) {
            int rowAffected = statement.executeUpdate(sqlQuery, statement.RETURN_GENERATED_KEYS);
            if (rowAffected == 1) {
                ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next()) {
                    employeeID = resultSet.getInt(1);
                }
            }
            return employeeID;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return -1;
    }

    private void addToPayroll(Connection connection, int employee_id, double salary) {
        try (Statement statement = connection.createStatement()) {
            double deductions = salary * 0.2;
            double taxablePay = salary - deductions;
            double tax = taxablePay * 0.1;
            double netPay = salary - tax;
            String sql = String.format("insert into payroll (employee_id,basic_pay,deductions,taxable_pay,tax,net_pay) values" +
                    "('%s','%s','%s','%s','%s','%s')", employee_id, salary, deductions, taxablePay, tax, netPay);
            statement.executeUpdate(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean addToDepartment(Connection connection, int employee_id, int[] departmentId, String[] departmentName) {
        try (Statement statement = connection.createStatement()) {
            for (int i = 0; i < departmentName.length; i++) {
                String sql = String.format("insert into department values ('%s','%s','%s')", departmentId[i], departmentName[i], employee_id);
                int rowAffected = statement.executeUpdate(sql);
                if (rowAffected != 1) {
                    return false;
                }
            }
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

}
