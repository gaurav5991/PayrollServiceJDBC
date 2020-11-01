package com.bridgelabz.employeepayroll;

import java.time.LocalDate;
import java.util.Objects;

public class EmployeePayroll {
    private int employeeId;
    private String employeeName;
    private double employeeSalary;
    private LocalDate startDate;

    /*Parameterized Constructor*/

    public EmployeePayroll(int employeeId, String employeeName, double employeeSalary) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.employeeSalary = employeeSalary;
    }

    public EmployeePayroll(int employeeId, String employeeName, double employeeSalary, LocalDate startDate) {
        this(employeeId,employeeName,employeeSalary);
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
