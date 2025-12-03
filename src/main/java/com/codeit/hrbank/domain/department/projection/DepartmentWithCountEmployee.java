package com.codeit.hrbank.domain.department.projection;

import java.time.LocalDate;

public interface DepartmentWithCountEmployee {
    Long getId();
    String getName();
    String getDescription();
    LocalDate getEstablishedDate();
    Long getEmployeeCount();
}
