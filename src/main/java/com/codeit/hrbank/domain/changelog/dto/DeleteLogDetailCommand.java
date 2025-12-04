package com.codeit.hrbank.domain.changelog.dto;

import com.codeit.hrbank.domain.employee.entity.Employee;
import lombok.Getter;

@Getter
public class DeleteLogDetailCommand {

    private final String hireDate;
    private final String name;
    private final String position;
    private final String department;
    private final String email;
    private final String status;

    public DeleteLogDetailCommand(
            Employee employee){
        this.hireDate = employee.getHireDate().toString();
        this.name = employee.getName();
        this.position = employee.getPosition();
        this.department = employee.getDepartment().getName();
        this.email = employee.getEmail();
        this.status = employee.getStatus().toString();
    }
}
