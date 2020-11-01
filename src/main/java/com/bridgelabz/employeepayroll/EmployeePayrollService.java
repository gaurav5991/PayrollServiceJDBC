package com.bridgelabz.employeepayroll;

import java.util.List;
import java.util.Scanner;

public class EmployeePayrollService {
    public enum IOService {
        CONSOLE_IO, FILE_IO, DB_IO
    }

    private List<EmployeePayroll> employeePayrollList;

    public EmployeePayrollService() { }

    public EmployeePayrollService(List<EmployeePayroll> employeePayrollList) {
        this.employeePayrollList = employeePayrollList;
    }
    /**
     * Method to write employee data using IOService
     *
     * @param ioService
     */
    public void writeEmployeePayrollData(IOService ioService) {
        if (ioService.equals(IOService.CONSOLE_IO))
            System.out.println("\nWriting Employee Payroll Roaster to console\n" + employeePayrollList);
        else if (ioService.equals(IOService.FILE_IO))
            new EmployeePayrollFileIOService().writeData(employeePayrollList);
    }

    /**
     * Method to read employee data from console
     *
     * @param ConsoleReaderInput
     */
    public void readEmployeePayrollData(Scanner ConsoleReaderInput) {
        Scanner consoleReaderInput = new Scanner(System.in);
        System.out.println("enter id: ");
        int id = consoleReaderInput.nextInt();
        System.out.println("Enter name: ");
        String name = consoleReaderInput.next();
        System.out.println("Enter salary: ");
        double salary = consoleReaderInput.nextDouble();
        EmployeePayroll employeePayroll = new EmployeePayroll(id, name, salary);
        employeePayrollList.add(employeePayroll);
    }

    /**
     *
     * Method to read employee data using IOService
     *
     * @param ioService
     * @return List
     */
    public List<EmployeePayroll> readEmployeePayrollData(IOService ioService) {
        if (ioService.equals(IOService.FILE_IO)) {
            this.employeePayrollList = new EmployeePayrollFileIOService().readData();
        } else if (ioService.equals(IOService.DB_IO)) {
            this.employeePayrollList = new EmployeePayrollFileDBService().readData();
        }
        return employeePayrollList;
    }

    /**
     * Method to print Employee data  from file or console
     *
     * @param ioService
     */
    public void printEmployeeData(IOService ioService) {
        if (ioService.equals(IOService.FILE_IO)) {
            new EmployeePayrollFileIOService().printData();
        } else if (ioService.equals(IOService.CONSOLE_IO))
            System.out.println("Writing to console\n" + employeePayrollList);
    }

    /**
     * @param ioService
     * @return Entry Count
     */
    public long countEmployeeEntries(IOService ioService) {
        long count = 0;
        if (ioService.equals(IOService.FILE_IO)) {
            count = new EmployeePayrollFileIOService().countData();
        } else if (ioService.equals(IOService.CONSOLE_IO)) {
            count = employeePayrollList.size();
        }
        return count;
    }
}
