package com.bridgelabz.employeepayroll;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

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

    public void addEmployeeToPayroll(List<EmployeePayroll> employeePayrollDataList) {
        employeePayrollDataList.forEach(employeePayroll -> {
            System.out.println("Employee being Added: " + employeePayroll.getEmployeeName());
            try {
                this.addEmployeePayrollData(employeePayroll.getEmployeeName(), employeePayroll.getEmployeeSalary(), employeePayroll.getEmployeeGender(), employeePayroll.getStartDate());
            } catch (EmployeePayrollException e) {
                e.printStackTrace();
            }
            System.out.println("Employee Added: " + employeePayroll.getEmployeeName());
        });
        System.out.println(this.employeePayrollList);
    }

    public void addEmployeePayrollData(String name, double salary, String gender, LocalDate startDate)
            throws EmployeePayrollException {
        employeePayrollList.add(employeePayrollFileDBService.
                addEmployeeToPayroll(name, gender, startDate, salary));
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
                addEmployeeToPayroll(id, name, phone, address, gender, startDate, salary, departmentId, departmentName));
    }

    /**
     * Metho to delete Employee From Payroll
     *
     * @param name
     * @return
     */
    public int deleteEmployeeFromPayroll(String name) throws EmployeePayrollException {
        employeePayrollFileDBService.deleteEmployee(name);
        employeePayrollList = this.employeePayrollList.stream().filter(employee -> !employee.getEmployeeName().equals(name)).collect(Collectors.toList());
        return employeePayrollList.size();
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

    /*Return Count Entries Added*/
    public int countEntries(IOService ioService) {
        if (ioService.equals(IOService.DB_IO)) {
            return employeePayrollList.size();
        }
        return 0;
    }

    /* Adding Employee Payroll using multithreads */
    public void addEmployeesToPayrollWithThreads(List<EmployeePayroll> employeePayrollDataList) {
        Map<Integer, Boolean> employeeAdditionStatus = new HashMap<Integer, Boolean>();
        employeePayrollDataList.forEach(employeePayrollData -> {
            Runnable task = () -> {
                employeeAdditionStatus.put(employeePayrollData.hashCode(), false);
                System.out.println("Employee Being Added: " + Thread.currentThread().getName());
                this.addEmployeeToPayroll(employeePayrollData.getEmployeeName(), employeePayrollData.getEmployeeSalary(),
                        employeePayrollData.getStartDate(), employeePayrollData.getEmployeeGender());
                employeeAdditionStatus.put(employeePayrollData.hashCode(), true);
                System.out.println("Employee Added " + Thread.currentThread().getName());
            };
            Thread thread = new Thread(task, employeePayrollData.getEmployeeName());
            thread.start();
        });
//        while (employeeAdditionStatus.containsValue(false)) {
//            try {
//                Thread.sleep(10);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
        System.out.println(employeePayrollDataList);

    }

    private void addEmployeeToPayroll(String employeeName, double employeeSalary, LocalDate startDate, String employeeGender) {
        employeePayrollList.forEach(employeePayrollData -> {
            System.out.println("Employee Being Added: " + employeePayrollData.getEmployeeName());
            this.addEmployeeToPayroll(employeePayrollData.getEmployeeName(), employeePayrollData.getEmployeeSalary(),
                    employeePayrollData.getStartDate(), employeePayrollData.getEmployeeGender());
            System.out.println("Employee Added:" + employeePayrollData.getEmployeeName());
            System.out.println(this.employeePayrollList);
        });
    }
}