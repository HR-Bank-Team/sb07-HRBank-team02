package com.codeit.hrbank.domain.employee.dto;

//직원 분포 정보 DTO
public record EmployeeDistributionDto(
        String groupKey,
        long count,
        double percentage
) {
}