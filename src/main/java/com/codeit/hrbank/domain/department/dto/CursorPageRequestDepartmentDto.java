package com.codeit.hrbank.domain.department.dto;

import org.springframework.data.domain.Sort;

public record CursorPageRequestDepartmentDto(
    String nameOrDescription,  // 검색 키워드 (기존 keyword)
    String cursor,             // 정렬 컬럼 마지막 값 (기존 lastValue)
    Long idAfter,              // 마지막 id (기존 lastId)
    Sort.Direction sortDirection, //desc or asc
    String sortField,          // 정렬 필드 name or establishedDate
    int size
) {

}
