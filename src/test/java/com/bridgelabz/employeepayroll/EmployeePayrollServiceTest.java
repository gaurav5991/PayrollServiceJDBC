package com.bridgelabz.employeepayroll;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.bridgelabz.employeepayroll.EmployeePayrollService.IOService.DB_IO;
import static com.bridgelabz.employeepayroll.EmployeePayrollService.IOService.FILE_IO;

public class EmployeePayrollServiceTest {
    EmployeePayrollService employeePayrollService;
    private List<EmployeePayroll> employeePayrollData;

    @Before
    public void setUp()  {
        employeePayrollService = new EmployeePayrollService();
    }

    /**
     * TestCase to check the number of entries write using writeEmployeeData
     */
    @Test
    public void givenThreeEmployeeWhenWrittenToFileShouldMatchEmployeeEntries() throws EmployeePayrollException {
        EmployeePayroll[] arrayOfEmps = {
                new EmployeePayroll(1, "Jeff Bezos", 10000000.0),
                new EmployeePayroll(2, "Bill Gates", 200000.00),
                new EmployeePayroll(2, "Satya Nadela", 300000.00)
        };
        employeePayrollService = new EmployeePayrollService(Arrays.asList(arrayOfEmps));
        employeePayrollService.writeEmployeePayrollData(FILE_IO);
        employeePayrollService.printEmployeeData(FILE_IO);
        long entries = employeePayrollService.countEmployeeEntries(FILE_IO);
        employeePayrollService.readEmployeePayrollData(FILE_IO);
        Assert.assertEquals(3, entries);
    }

    /**
     * TestCase to check the count of data read from file
     */
    @Test
    public void givenFileOnReadingFromFileShouldMatchEmployeeCount() throws EmployeePayrollException {
        employeePayrollData = employeePayrollService.readEmployeePayrollData(FILE_IO);
        Assert.assertEquals(3, employeePayrollData.size());
    }

    /**
     * TestCase to check the number of entries read from database using JDBC
     */
    @Test
    public void givenEmployeePayrollInDBWhenRetrievedShouldMatchEmployeeCount() throws EmployeePayrollException {
        employeePayrollData = employeePayrollService.readEmployeePayrollData(DB_IO);
        Assert.assertEquals(3, employeePayrollData.size());
    }

    /**
     * TestCase to check Salary of Employee is Updated or not Using UpdateEmployeeSalary Method
     */
    @Test
    public void givenNewSalaryOfEmployeeWhenUpdatedShouldMatch() throws EmployeePayrollException {
        employeePayrollData = employeePayrollService.readEmployeePayrollData(DB_IO);
        employeePayrollService.updateEmployeeSalary("Terisa", 4000000.00);
        boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Terisa");
        Assert.assertTrue(result);
    }

    /**
     * TestCase to check the employee count who joined in given date range
     */
    @Test
    public void givenDateRangeWhenRetrievedShouldMatchEmployeeCount() throws EmployeePayrollException {
        employeePayrollService.readEmployeePayrollData(DB_IO);
        LocalDate startDate = LocalDate.of(2019,01,01);
        LocalDate endDate = LocalDate.now();
        employeePayrollData = employeePayrollService.readEmployeePayrollForDateRange(DB_IO, startDate, endDate);
        Assert.assertEquals(1,employeePayrollData.size());
    }

    /**
     * TestCase to check that method returns right average salary
     */
    @Test
    public void givenPayrollDataWhenAverageSalaryRetrievedByGenderShouldReturnProperValue() throws EmployeePayrollException {
        employeePayrollService.readEmployeePayrollData(DB_IO);
        Map<String,Double> averageSalaryBygender = employeePayrollService.readAverageSalaryByGender(DB_IO);
        Assert.assertTrue(averageSalaryBygender.get("M").equals(2000000.00) && averageSalaryBygender.get("F").equals(4000000.00));
    }
    /**
     * TestCase to check that method returns right count of employee based on gender
     */
    @Test
    public void givenPayrollData_whenAverageSalaryRetrievedByGender_shouldReturnProperCountValue() throws EmployeePayrollException {
        employeePayrollService.readEmployeePayrollData(DB_IO);
        Map<String, Double> countByGender = employeePayrollService.readCountByGender(DB_IO);
        Assert.assertTrue(countByGender.get("M").equals(2.0) && countByGender.get("F").equals(1.0));
    }
    /**
     * TestCase to check that method returns right Minimum salary
     */
    @Test
    public void givenPayrollData_whenAverageSalaryRetrievedByGender_shouldReturnProperMinimumValue() throws EmployeePayrollException {
        employeePayrollService.readEmployeePayrollData(DB_IO);
        Map<String, Double> countByGender = employeePayrollService.readMinumumSalaryByGender(DB_IO);
        Assert.assertTrue(countByGender.get("M").equals(50000.00) && countByGender.get("F").equals(4000000.00));
    }
    /**
     * TestCase to check that method returns right Maximum salary
     */
    @Test
    public void givenPayrollData_whenAverageSalaryRetrievedByGender_shouldReturnProperMaximumValue() throws EmployeePayrollException {
        employeePayrollService.readEmployeePayrollData(DB_IO);
        Map<String, Double> countByGender = employeePayrollService.readMaximumSalaryByGender(DB_IO);
        Assert.assertTrue(countByGender.get("M").equals(3000000.00) && countByGender.get("F").equals(4000000.00));
    }
    /**
     * TestCase to check that method returns right sum of salary
     */
    @Test
    public void givenPayrollData_whenAverageSalaryRetrievedByGender_shouldReturnProperSumValue() throws EmployeePayrollException {
        employeePayrollService.readEmployeePayrollData(DB_IO);
        Map<String, Double> sumSalaryByGender = employeePayrollService.readSumSalaryByGender(DB_IO);
        Assert.assertTrue(sumSalaryByGender.get("M").equals(4000000.00) && sumSalaryByGender.get("F").equals(4000000.00));
    }

    /**
     * Test Case to check new Employee Added Successfully to Database and in sync
     */
    @Test
    public void givenEmployee_DBWhenAdded_ShouldSyncWithDB() throws EmployeePayrollException {
        employeePayrollService.readEmployeePayrollData(DB_IO);
        employeePayrollService.addEmployeePayrollData(108,"Adam","7894568901","xyz street","M",LocalDate.now(),50000.00,125,"Marketing");
        boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Adam");
        Assert.assertTrue(result);
    }

    /**
     * Test Case to check employee deleted or not
     */
    @Test
    public void givenEmployee_whenDeleted_shouldBeInSyncWithDB() throws EmployeePayrollException {
        employeePayrollService.readEmployeePayrollData(DB_IO);
        int result = employeePayrollService.deleteEmployeeFromPayroll("Adam");
        Assert.assertEquals(3,result);
    }
    
    @Test
    public void givenSixEmployeeWhenWrittenToFileShouldMatchEmployeeEntries() throws EmployeePayrollException {
        EmployeePayroll[] arrayOfEmps = {
                new EmployeePayroll(0, "Jeff Bezos", "M",10000000.0,LocalDate.now()),
                new EmployeePayroll(0, "Bill Gates", "M",200000.00,LocalDate.now()),
                new EmployeePayroll(0, "Satya Nadela","M", 300000.00,LocalDate.now()),
                new EmployeePayroll(0, "Sunder", "M",600000.00,LocalDate.now()),
                new EmployeePayroll(0, "Mukesh", "M",1000000.00,LocalDate.now()),
                new EmployeePayroll(0, "Anil", "M",200000.00,LocalDate.now())
        };
        employeePayrollService.readEmployeePayrollData(DB_IO);
        Instant start = Instant.now();
        employeePayrollService.addEmployeeToPayroll(Arrays.asList(arrayOfEmps));
        Instant end = Instant.now();
        System.out.println("Duration without Thread: "+ Duration.between(start,end));
        Instant threadStart = Instant.now();
        employeePayrollService.addEmployeesToPayrollWithThreads(Arrays.asList(arrayOfEmps));
        Instant threadEnd = Instant.now();
        System.out.println("Duration with Thread; " + Duration.between(threadStart, threadEnd));
        Assert.assertEquals(7,employeePayrollService.countEntries(DB_IO));
    }

    @Test
    public void given2Employees_whenAddedToDB_shouldMatchEmployeeEntries() throws InterruptedException, EmployeePayrollException {
        EmployeePayroll[] arrayOfEmployees = {
                new EmployeePayroll(0,"sharad",LocalDate.now(),"2345678901","F","Jaipur",50000.00, new int[]{102}, new String[]{"Dept2"}),
                new EmployeePayroll(0, "shukla", LocalDate.now(),"34343434", "F","Bhopal", 3000000.0 , new int[]{101}, new String[]{"Dept1"}),
        };
        employeePayrollService.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
        Instant threadStart = Instant.now();
        employeePayrollService.addEmployeesToPayrollWithThreads(Arrays.asList(arrayOfEmployees));
        Thread.sleep(6000);
        Instant threadEnd = Instant.now();
        System.out.println("Duration for complete execution with Threads: " + Duration.between(threadStart, threadEnd));
        Assert.assertEquals(11, employeePayrollService.countEntries(EmployeePayrollService.IOService.DB_IO));
    }

}