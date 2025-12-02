package com.codeit.hrbank.domain.backup.mapper;

import com.codeit.hrbank.domain.backup.dto.export.ExportEmployeeDto;
import com.codeit.hrbank.domain.employee.entity.Employee;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ExportEmployeeMapper {

    public ExportEmployeeDto toDto(Employee employee){
        return new ExportEmployeeDto(
                employee.getId(),
                employee.getName(),
                employee.getEmail(),
                employee.getEmployeeNumber(),
                employee.getDepartment().getId(),
                employee.getDepartment().getName(),
                employee.getPosition(),
                LocalDateTime.from( employee.getHireDate()),
                employee.getStatus()

        );
    }
}
