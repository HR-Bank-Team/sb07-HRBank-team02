package com.codeit.hrbank.domain.employee.dto;

public record EmployeeDistributionDto(
        String groupKey,
        long count,
        double percentage
) {
}