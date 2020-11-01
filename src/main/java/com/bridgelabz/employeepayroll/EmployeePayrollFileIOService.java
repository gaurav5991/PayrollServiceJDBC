package com.bridgelabz.employeepayroll;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class EmployeePayrollFileIOService {
    public static String PAYROLL_FILE_NAME = "payroll-file.txt";

    Path filepath = Paths.get(PAYROLL_FILE_NAME);

    public void writeData(List<EmployeePayroll> employeePayrollList) throws EmployeePayrollException {
        System.out.println(employeePayrollList);
        StringBuffer stringBuffer = new StringBuffer();
        employeePayrollList.forEach(employee -> {
            String employeeDataString = employee.toString().concat("\n");
            stringBuffer.append(employeeDataString);
        });
        try {
            Files.write(filepath,stringBuffer.toString().getBytes());
        }catch (IOException e){
          throw new EmployeePayrollException(e.getMessage(), EmployeePayrollException.ExceptionType.UNABLE_TO_WRITE);
        }
    }

    public List<EmployeePayroll> readData() throws EmployeePayrollException {
        List<EmployeePayroll> employeePayrollList = new ArrayList<>();
        try{
            List<String> contents = Files.readAllLines(filepath);
            for (String content:contents){
                String trimmed_content = content.trim();
                String[] split_content = trimmed_content.split(",");
                for (int i = 0; i < split_content.length; i++) {
                    int id = Integer.parseInt(split_content[i].replaceAll("id=",""));
                    i++;
                    String name = split_content[i].replaceAll("name=","");
                    i++;
                    double salary = Double.parseDouble(split_content[i].replaceAll("salary=",""));
                    EmployeePayroll employeePayroll = new EmployeePayroll(id, name, salary);
                    employeePayrollList.add(employeePayroll);
                }
            }
        }catch (IOException e){
            throw new EmployeePayrollException(e.getMessage(), EmployeePayrollException.ExceptionType.UNABLE_TO_READ);
        }
        return employeePayrollList;
    }

    public void printData() {
        try {

            List<String> strings = Files.readAllLines(filepath);
            for (String string: strings){
                System.out.println(string);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public long countData() {
        long count = 0;
        try{
            count = Files.lines(filepath).count();
        }catch (IOException e){
            e.printStackTrace();
        }
        return count;
    }
}
