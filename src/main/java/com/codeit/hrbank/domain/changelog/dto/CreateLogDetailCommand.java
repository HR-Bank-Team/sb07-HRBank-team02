package com.codeit.hrbank.domain.changelog.dto;

import com.codeit.hrbank.domain.employee.entity.Employee;
import lombok.Getter;

@Getter
public class CreateLogDetailCommand {

    private String hireDate;
    private String name;
    private String position;
    private String department;
    private String email;
    private String employeeNumber;
    private String status;

    public CreateLogDetailCommand(
            Employee employee){
        this.hireDate = employee.getHireDate().toString();
        this.name = employee.getName();
        this.position = employee.getPosition();
        this.department = employee.getDepartment().getName();
        this.email = employee.getEmail();
        this.employeeNumber = employee.getEmployeeNumber();
        this.status = employee.getStatus().toString();
    }
}
