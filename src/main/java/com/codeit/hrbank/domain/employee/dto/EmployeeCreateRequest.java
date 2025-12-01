package com.codeit.hrbank.domain.employee.dto;

import java.time.LocalDateTime;

public record EmployeeCreateRequest(
        String name,
        String email,
        Long departmentId,
        String position,
        LocalDateTime hireDate,
        String memo
) {
}
