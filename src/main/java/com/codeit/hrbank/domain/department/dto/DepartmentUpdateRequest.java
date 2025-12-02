package com.codeit.hrbank.domain.department.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record DepartmentUpdateRequest(
        @NotBlank String name,
        @NotBlank String description,
        @NotBlank LocalDate establishedDate
) {
}
