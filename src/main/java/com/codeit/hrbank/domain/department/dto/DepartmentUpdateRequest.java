package com.codeit.hrbank.domain.department.dto;

public record DepartmentUpdateRequest(
        String name,
        String description,
        String establishedDate
) {
}
