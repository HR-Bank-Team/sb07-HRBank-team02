package com.codeit.hrbank.domain.changelog.dto;

import com.codeit.hrbank.domain.employee.entity.Employee;
import lombok.Getter;

@Getter
public class CreateLogDetailCommand {

    private final String hireDate;
    private final String name;
    private final String position;
    private final String department;
    private final String email;
    private final String employeeNumber;
    private final String status;

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
