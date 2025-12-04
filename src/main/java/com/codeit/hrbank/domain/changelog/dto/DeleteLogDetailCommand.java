package com.codeit.hrbank.domain.changelog.dto;

import com.codeit.hrbank.domain.employee.entity.Employee;
import lombok.Getter;

@Getter
public class DeleteLogDetailCommand {

    private String hireDate;
    private String name;
    private String position;
    private String department;
    private String email;
    private String status;

    public DeleteLogDetailCommand(
            Employee employee){
        this.hireDate = employee.getHireDate().toString();
        this.name = employee.getName();
        this.position = employee.getPosition();
        this.department = employee.getDepartment().toString();
        this.email = employee.getEmail();
        this.status = employee.getStatus().toString();
    }
}
