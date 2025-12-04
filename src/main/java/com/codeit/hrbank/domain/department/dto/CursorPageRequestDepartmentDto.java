package com.codeit.hrbank.domain.department.dto;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

public record CursorPageRequestDepartmentDto(
        String nameOrDescription,  // 검색 키워드
        String cursor,             // 정렬 컬럼 마지막 값
        Long idAfter,              // 마지막 id
        String sortDirection, //desc or asc
        String sortField,          // 정렬 필드 name or establishedDate
        Integer size
) {
    public Direction sortDirectionOrDefault() {
        if (sortDirection == null || sortDirection.isBlank()) {
            return Direction.DESC;
        }
        // Direction.fromString 은 대소문자 무시함 ("asc", "ASC", "Asc" 전부 OK)
        return Direction.fromString(sortDirection);
    }

    public String sortFieldOrDefault() {
        return (sortField == null || sortField.isBlank()) ? "establishedDate" : sortField;
    }

    public int sizeOrDefault() {
        return (size == null || size <= 0) ? 10 : size;
    }

    public Pageable toPageable() {
        Direction direction = sortDirectionOrDefault();
        Sort sort = Sort.by(direction, sortFieldOrDefault())
                .and(Sort.by(direction, "id"));
        return PageRequest.of(0, sizeOrDefault(), sort);
    }
}
