package com.bridgelabz.employeepayroll;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class EmployeePayrollService {

    public enum IOService {
        CONSOLE_IO, FILE_IO, DB_IO
    }

    private List<EmployeePayroll> employeePayrollList;
    private EmployeePayrollFileDBService employeePayrollFileDBService;

    public EmployeePayrollService() {
        employeePayrollFileDBService = EmployeePayrollFileDBService.getInstance();
    }

    public EmployeePayrollService(List<EmployeePayroll> employeePayrollList) {
        this();
        this.employeePayrollList = employeePayrollList;
    }

    /**
     * Method to write employee data using IOService
     *
     * @param ioService
     */
    public void writeEmployeePayrollData(IOService ioService) throws EmployeePayrollException {
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
     * Method to update Employee Salary  by name
     *
     * @param name
     * @param salary
     */
    public void updateEmployeeSalary(String name, double salary) throws EmployeePayrollException {
        int result = employeePayrollFileDBService.updateEmployeeData(name, salary);
        if (result == 0) {
            try {
                throw new EmployeePayrollException(EmployeePayrollException.ExceptionType.UNABLE_TO_UPDATE);
            } catch (EmployeePayrollException e) {
                e.printStackTrace();
            }
        }
        EmployeePayroll employeePayroll = this.getEmployeePayrollData(name);
        if (employeePayroll != null)
            employeePayroll.setEmployeeSalary(salary);
    }

    /**
     * @param name
     * @param salary
     * @param startDate
     * @param gender
     * @throws EmployeePayrollException
     */
    public void addEmployeePayrollData(int id, String name, String phone, String address, String gender, LocalDate startDate,
                                       double salary, int departmentId, String departmentName)
            throws EmployeePayrollException {
        employeePayrollList.add(employeePayrollFileDBService.
                addEmployeeToPayroll(id,name,phone,address,gender,startDate,salary,departmentId,departmentName));
    }

    /**
     * Method to get Employee payroll data
     *
     * @param name
     * @return
     */
    private EmployeePayroll getEmployeePayrollData(String name) {
        return this.employeePayrollList.stream()
                .filter(employeePayrollDataItem -> employeePayrollDataItem.getEmployeeName().equals(name))
                .findFirst()
                .orElse(null);
    }

    /**
     * Method to check if DB in sync with EmployeePayroll
     *
     * @param name
     * @return
     */
    public boolean checkEmployeePayrollInSyncWithDB(String name) throws EmployeePayrollException {
        List<EmployeePayroll> employeePayrollList = employeePayrollFileDBService.getEmployeepayrollData(name);
        return employeePayrollList.get(0).equals(getEmployeePayrollData(name));
    }

    /**
     * Method to read employee data using IOService
     *
     * @param ioService
     * @return List
     */
    public List<EmployeePayroll> readEmployeePayrollData(IOService ioService) throws EmployeePayrollException {
        if (ioService.equals(IOService.FILE_IO)) {
            this.employeePayrollList = new EmployeePayrollFileIOService().readData();
        } else if (ioService.equals(IOService.DB_IO)) {
            this.employeePayrollList = employeePayrollFileDBService.readData();
        }
        return employeePayrollList;
    }

    /**
     * Method to read employee data in given date range
     *
     * @param ioService
     * @param startDate
     * @param endDate
     * @return
     */
    public List<EmployeePayroll> readEmployeePayrollForDateRange(IOService ioService, LocalDate startDate, LocalDate endDate)
            throws EmployeePayrollException {
        if (ioService.equals(IOService.DB_IO))
            return employeePayrollFileDBService.getEmployeepayrollForDateRange(startDate, endDate);
        return null;
    }

    /**
     * Method to get Average salary by Gender
     *
     * @param ioService
     * @return
     */
    public Map<String, Double> readAverageSalaryByGender(IOService ioService) {
        if (ioService.equals(IOService.DB_IO))
            return employeePayrollFileDBService.getAverageSalaryByGender();
        return null;
    }

    /**
     * Method to get Count by Gender
     *
     * @param ioService
     * @return
     */
    public Map<String, Double> readCountByGender(IOService ioService) {
        if (ioService.equals(IOService.DB_IO))
            return employeePayrollFileDBService.getCountByGender();
        return null;
    }

    /**
     * Method to get minimum salary by Gender
     *
     * @param ioService
     * @return
     */
    public Map<String, Double> readMinumumSalaryByGender(IOService ioService) {
        if (ioService.equals(IOService.DB_IO))
            return employeePayrollFileDBService.getMinimumByGender();
        return null;
    }

    /**
     * Method to get Maximum salary by Gender
     *
     * @param ioService
     * @return
     */
    public Map<String, Double> readMaximumSalaryByGender(IOService ioService) {
        if (ioService.equals(IOService.DB_IO))
            return employeePayrollFileDBService.getMaximumByGender();
        return null;
    }

    /**
     * Method to get Sum of salary by Gender
     *
     * @param ioService
     * @return
     */
    public Map<String, Double> readSumSalaryByGender(IOService ioService) {
        if (ioService.equals(IOService.DB_IO))
            return employeePayrollFileDBService.getSalarySumByGender();
        return null;
    }

    /**
     * Method to print Employee data  from file or console
     *
     * @param ioService
     */
    public void printEmployeeData(IOService ioService) throws EmployeePayrollException {
        if (ioService.equals(IOService.FILE_IO)) {
            new EmployeePayrollFileIOService().printData();
        } else if (ioService.equals(IOService.CONSOLE_IO))
            System.out.println("Writing to console\n" + employeePayrollList);
    }

    /**
     * Method to count Number of Entry for FILE_IO and CONSOLE_IO
     *
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