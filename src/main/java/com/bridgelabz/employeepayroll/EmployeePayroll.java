package com.bridgelabz.employeepayroll;

import java.time.LocalDate;
import java.util.Objects;

public class EmployeePayroll {
    private int employeeId;
    private String employeeName;
    private String employeePhone;
    private String employeeAddress;
    private String employeeGender;
    private double employeeSalary;
    private LocalDate startDate;

    /*Parameterized Constructor*/

    public EmployeePayroll(int employeeId, String employeeName, double employeeSalary) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.employeeSalary = employeeSalary;
    }

    public EmployeePayroll(int employeeId, String employeeName, double employeeSalary, LocalDate startDate) {
        this(employeeId, employeeName, employeeSalary);
        this.startDate = startDate;
    }

    public EmployeePayroll(int employeeId, String employeeName, String employeeGender, double employeeSalary, LocalDate startDate) {
        this(employeeId, employeeName, employeeSalary, startDate);
        this.employeeGender = employeeGender;
    }

    public EmployeePayroll(int employeeId, String employeeName, String employeePhone, String employeeAddress, String employeeGender, double employeeSalary, LocalDate startDate) {
        this(employeeId, employeeName, employeeSalary, startDate);
        this.employeePhone = employeePhone;
        this.employeeAddress = employeeAddress;
        this.employeeGender = employeeGender;
    }

    public EmployeePayroll(String employeeName, String employeeGender, double employeeSalary, LocalDate startDate) {
        this.employeeName = employeeName;
        this.employeeGender = employeeGender;
        this.employeeSalary = employeeSalary;
        this.startDate = startDate;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public double getEmployeeSalary() {
        return employeeSalary;
    }

    public void setEmployeeSalary(double employeeSalary) {
        this.employeeSalary = employeeSalary;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public String getEmployeePhone() {
        return employeePhone;
    }

    public void setEmployeePhone(String employeePhone) {
        this.employeePhone = employeePhone;
    }

    public String getEmployeeAddress() {
        return employeeAddress;
    }

    public void setEmployeeAddress(String employeeAddress) {
        this.employeeAddress = employeeAddress;
    }

    public String getEmployeeGender() {
        return employeeGender;
    }

    public void setEmployeeGender(String employeeGender) {
        this.employeeGender = employeeGender;
    }

    @Override
    public String toString() {
        return "id=" + employeeId +
                ", name='" + employeeName + '\'' +
                ", salary=" + employeeSalary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeePayroll that = (EmployeePayroll) o;
        return employeeId == that.employeeId &&
                Double.compare(that.employeeSalary, employeeSalary) == 0 &&
                Objects.equals(employeeName, that.employeeName) &&
                Objects.equals(startDate, that.startDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeId, employeeName, employeeSalary, startDate);
    }
}
