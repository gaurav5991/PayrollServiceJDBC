package com.bridgelabz.employeepayroll;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.bridgelabz.employeepayroll.EmployeePayrollService.IOService.DB_IO;
import static com.bridgelabz.employeepayroll.EmployeePayrollService.IOService.FILE_IO;

public class EmployeePayrollServiceTest {
    EmployeePayrollService employeePayrollService;

    /**
     * TestCase to check the number of entries write using writeEmployeeData
     */
    @Test
    public void givenThreeEmployeeWhenWrittenToFileShouldMatchEmployeeEntries() {
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
    public void givenFileOnReadingFromFileShouldMatchEmployeeCount() {
        employeePayrollService = new EmployeePayrollService();
        List<EmployeePayroll> employeePayrollData = employeePayrollService.readEmployeePayrollData(FILE_IO);
        Assert.assertEquals(3, employeePayrollData.size());
    }

    /**
     * TestCase to check the number of entries read from database using JDBC
     */
    @Test
    public void givenEmployeePayrollInDBWhenRetrievedShouldMatchEmployeeCount() {
        employeePayrollService = new EmployeePayrollService();
        List<EmployeePayroll> employeePayrollData = employeePayrollService.readEmployeePayrollData(DB_IO);
        Assert.assertEquals(3, employeePayrollData.size());
    }
}