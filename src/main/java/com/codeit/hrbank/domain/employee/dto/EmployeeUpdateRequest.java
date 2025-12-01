package com.codeit.hrbank.domain.employee.dto;

import com.codeit.hrbank.domain.employee.entity.EmployeeStatus;

import java.time.LocalDateTime;

public record EmployeeUpdateRequest(
        String name,
        String email,
        Long departmentId,
        String position,
        LocalDateTime hireDate,
        EmployeeStatus status,
        String memo
) {}
