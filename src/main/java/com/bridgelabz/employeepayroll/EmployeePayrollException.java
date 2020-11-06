package com.bridgelabz.employeepayroll;

public class EmployeePayrollException extends Exception {
    enum ExceptionType{
        UNABLE_TO_UPDATE, UNABLE_TO_WRITE, UNABLE_TO_READ, UNABLE_TO_CONNECT,
        PROBLEM_IN_RESULTSET, SQLEXCEPTION,UNABLE_TO_PRINT
    }
    ExceptionType type;

    public EmployeePayrollException(String message, ExceptionType type) {
        super(message);
        this.type = type;
    }

    public EmployeePayrollException(ExceptionType type) {
        this.type = type;
    }
}
