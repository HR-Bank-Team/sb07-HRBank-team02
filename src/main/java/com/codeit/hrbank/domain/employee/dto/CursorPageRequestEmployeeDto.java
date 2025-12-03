package com.codeit.hrbank.domain.employee.dto;

import com.codeit.hrbank.domain.employee.entity.EmployeeStatus;

import java.time.LocalDate;

public record CursorPageRequestEmployeeDto(
        String nameOrEmail,
        String employeeNumber,
        String departmentName,
        String position,
        LocalDate hireDateFrom,
        LocalDate hireDateTo,
        EmployeeStatus status,
        Long idAfter, // 이젠 페이지 마지막 요소 ID
        String cursor, // 커서(다음 페이지 시작점)
        Integer size, // 페이지 크기
        SortField sortField, // 정렬 필드 (name, employeeNumber, hireDate)
        SortDirection sortDirection // 정렬 방향 (asc, desc 기본값: asc)
) {
    public CursorPageRequestEmployeeDto(
        String nameOrEmail,
        String employeeNumber,
        String departmentName,
        String position,
        LocalDate hireDateFrom,
        LocalDate hireDateTo,
        EmployeeStatus status,
        Long idAfter,
        String cursor,
        Integer size,
        SortField sortField,
        SortDirection sortDirection
    ) {
        this.nameOrEmail = nameOrEmail;
        this.employeeNumber = employeeNumber;
        this.departmentName = departmentName;
        this.position = position;
        this.hireDateFrom = hireDateFrom;
        this.hireDateTo = hireDateTo;
        this.status = status;
        this.idAfter = idAfter;
        this.cursor = cursor;
        this.size = size == null ? 10 : size;
        this.sortField = sortField == null ? SortField.name : sortField;
        this.sortDirection = sortDirection == null ? SortDirection.asc : sortDirection;
    }
}
