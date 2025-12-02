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

        // 프로필이 없는 경우 null 저장
        Long profileId = employee.getProfile() != null ? employee.getProfile().getId() : null;


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
                profileId
        );
    }

    public List<EmployeeDto> toDto(List<Employee> employees) {
        return employees.stream()
                .map(employee -> {
                    Department department = employee.getDepartment();

                    // 프로필이 없는 경우 null 저장
                    Long profileId = employee.getProfile() != null ? employee.getProfile().getId() : null;

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
                            profileId
                    );
                })
                .toList();
    }
}
