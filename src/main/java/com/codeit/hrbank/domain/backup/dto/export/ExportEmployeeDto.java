package com.codeit.hrbank.domain.backup.dto.export;

import com.codeit.hrbank.domain.employee.entity.EmployeeStatus;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record ExportEmployeeDto(
        Long id,
        String name,
        String email,
        String employeeNumber,
        Long departmentId,
        String departmentName,
        String position,
        LocalDate hireDate,
        EmployeeStatus status
) implements Serializable {
}
