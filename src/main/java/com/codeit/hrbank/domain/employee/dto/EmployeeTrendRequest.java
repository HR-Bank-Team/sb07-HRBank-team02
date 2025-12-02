package com.codeit.hrbank.domain.employee.dto;

import java.time.LocalDate;

public record EmployeeTrendRequest(
        LocalDate from,
        LocalDate to,
        TimeUnit unit
) {}
