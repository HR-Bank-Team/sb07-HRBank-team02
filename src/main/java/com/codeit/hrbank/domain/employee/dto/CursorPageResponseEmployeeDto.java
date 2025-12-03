package com.codeit.hrbank.domain.employee.dto;

public record CursorPageResponseEmployeeDto(
        Object content,
        String nextCursor,
        Long nextIdAfter,
        Integer size,
        Integer totalElements,
        boolean hasNext
) {}
