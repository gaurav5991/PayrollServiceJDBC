package com.bridgelabz.employeepayroll;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.bridgelabz.employeepayroll.EmployeePayrollService.IOService.DB_IO;
import static com.bridgelabz.employeepayroll.EmployeePayrollService.IOService.FILE_IO;

public class EmployeePayrollServiceTest {
    EmployeePayrollService employeePayrollService;

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
        employeePayrollService = new EmployeePayrollService();
        List<EmployeePayroll> employeePayrollData = employeePayrollService.readEmployeePayrollData(FILE_IO);
        Assert.assertEquals(3, employeePayrollData.size());
    }

    /**
     * TestCase to check the number of entries read from database using JDBC
     */
    @Test
    public void givenEmployeePayrollInDBWhenRetrievedShouldMatchEmployeeCount() throws EmployeePayrollException {
        employeePayrollService = new EmployeePayrollService();
        List<EmployeePayroll> employeePayrollData = employeePayrollService.readEmployeePayrollData(DB_IO);
        Assert.assertEquals(3, employeePayrollData.size());
    }

    /**
     * TestCase to check Salary of Employee is Updated or not Using UpdateEmployeeSalary Method
     */
    @Test
    public void givenNewSalaryOfEmployeeWhenUpdatedShouldMatch() throws EmployeePayrollException {
        employeePayrollService = new EmployeePayrollService();
        List<EmployeePayroll> employeePayrollData = employeePayrollService.readEmployeePayrollData(DB_IO);
        employeePayrollService.updateEmployeeSalary("Terisa", 3000000.00);
        boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Terisa");
        Assert.assertTrue(result);
    }

    /**
     * TestCase to check the employee count who joined in given date range
     */
    @Test
    public void givenDateRangeWhenRetrievedShouldMatchEmployeeCount() throws EmployeePayrollException {
        employeePayrollService = new EmployeePayrollService();
        employeePayrollService.readEmployeePayrollData(DB_IO);
        LocalDate startDate = LocalDate.of(2018,01,01);
        LocalDate endDate = LocalDate.now();
        List<EmployeePayroll> employeePayrollData = employeePayrollService.readEmployeePayrollForDateRange(DB_IO, startDate, endDate);
        Assert.assertEquals(3,employeePayrollData.size());
    }

    /**
     * TestCase to check that method returns right average salary
     */
    @Test
    public void givenPayrollDataWhenAVerageSalaryRetrievedByGenderShouldReturnProperValue() throws EmployeePayrollException {
        employeePayrollService = new EmployeePayrollService();
        employeePayrollService.readEmployeePayrollData(DB_IO);
        Map<String,Double> averageSalaryBygender = employeePayrollService.readAverageSalaryByGender(DB_IO);
        Assert.assertTrue(averageSalaryBygender.get("M").equals(2000000.00) && averageSalaryBygender.get("F").equals(3000000.00));
    }
    /**
     * TestCase to check that method returns right count of employee based on gender
     */
    @Test
    public void givenPayrollData_whenAverageSalaryRetrievedByGender_shouldReturnProperCountValue() throws EmployeePayrollException {
        employeePayrollService = new EmployeePayrollService();
        employeePayrollService.readEmployeePayrollData(DB_IO);
        Map<String, Double> countByGender = employeePayrollService.readCountByGender(DB_IO);
        Assert.assertTrue(countByGender.get("M").equals(2.0) && countByGender.get("F").equals(1.0));
    }
    /**
     * TestCase to check that method returns right Minimum salary
     */
    @Test
    public void givenPayrollData_whenAverageSalaryRetrievedByGender_shouldReturnProperMinimumValue() throws EmployeePayrollException {
        employeePayrollService = new EmployeePayrollService();
        employeePayrollService.readEmployeePayrollData(DB_IO);
        Map<String, Double> countByGender = employeePayrollService.readMinumumSalaryByGender(DB_IO);
        Assert.assertTrue(countByGender.get("M").equals(1000000.00) && countByGender.get("F").equals(3000000.00));
    }
    /**
     * TestCase to check that method returns right Maximum salary
     */
    @Test
    public void givenPayrollData_whenAverageSalaryRetrievedByGender_shouldReturnProperMaximumValue() throws EmployeePayrollException {
        employeePayrollService = new EmployeePayrollService();
        employeePayrollService.readEmployeePayrollData(DB_IO);
        Map<String, Double> countByGender = employeePayrollService.readMaximumSalaryByGender(DB_IO);
        Assert.assertTrue(countByGender.get("M").equals(3000000.00) && countByGender.get("F").equals(3000000.00));
    }
    /**
     * TestCase to check that method returns right sum of salary
     */
    @Test
    public void givenPayrollData_whenAverageSalaryRetrievedByGender_shouldReturnProperSumValue() throws EmployeePayrollException {
        employeePayrollService = new EmployeePayrollService();
        employeePayrollService.readEmployeePayrollData(DB_IO);
        Map<String, Double> sumSalaryByGender = employeePayrollService.readSumSalaryByGender(DB_IO);
        Assert.assertTrue(sumSalaryByGender.get("M").equals(4000000.00) && sumSalaryByGender.get("F").equals(3000000.00));
    }

    @Test
    public void givenEmployeeDBWhenAddedShouldSyncWithDB() throws EmployeePayrollException {
        employeePayrollService=new EmployeePayrollService();
        employeePayrollService.readEmployeePayrollData(IOService.DB_IO);
        employeePayrollService.addEmployeePayrollData("Mark",5000000.00,LocalDate.now(),"M");
        boolean result=employeePayrollService.checkEmployeePayrollInSyncWithDB("Mark");
        Assert.assertTrue(result);

    }
}