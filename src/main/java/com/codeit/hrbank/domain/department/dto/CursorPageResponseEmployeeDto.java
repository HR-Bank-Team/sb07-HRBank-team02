package com.codeit.hrbank.domain.department.dto;

import java.util.List;

public record CursorPageResponseEmployeeDto(
        List<DepartmentDto> content,
        String nextCursor,
        Long nextIdAfter,
        Integer size,
        Long totalElements,
        boolean hasNext
) {
}
