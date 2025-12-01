package com.codeit.hrbank.domain.employee.mapper;


import com.codeit.hrbank.domain.department.entity.Department;
import com.codeit.hrbank.domain.employee.dto.EmployeeDto;
import com.codeit.hrbank.domain.employee.entity.Employee;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EmployeeMapper {

    public EmployeeDto toDto(Employee employee) {
        Department department = employee.getDepartment();

        return new EmployeeDto(
                employee.getId(),
                employee.getName(),
                employee.getEmail(),
                employee.getEmployeeNumber(),
                department.getId(),
                department.getName(),
                employee.getPosition(),
                employee.getHireDate(),
                employee.getStatus(),
                employee.getProfile().getId()
        );
    }

    public List<EmployeeDto> toDto(List<Employee> employees) {
        return employees.stream()
                .map(employee -> {
                    Department department = employee.getDepartment();

                    return new EmployeeDto(
                            employee.getId(),
                            employee.getName(),
                            employee.getEmail(),
                            employee.getEmployeeNumber(),
                            department.getId(),
                            department.getName(),
                            employee.getPosition(),
                            employee.getHireDate(),
                            employee.getStatus(),
                            employee.getProfile().getId()
                    );
                })
                .toList();
    }
}
