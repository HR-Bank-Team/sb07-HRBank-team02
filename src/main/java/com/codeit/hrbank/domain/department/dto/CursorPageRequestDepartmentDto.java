package com.codeit.hrbank.domain.department.dto;

import org.springframework.data.domain.Sort.Direction;

public record CursorPageRequestDepartmentDto(
    String nameOrDescription,  // 검색 키워드 (기존 keyword)
    String cursor,             // 정렬 컬럼 마지막 값 (기존 lastValue)
    Long idAfter,              // 마지막 id (기존 lastId)
    Direction sortDirection, //desc or asc
    String sortField,          // 정렬 필드 name or establishedDate
    Integer size
) {
  // 커스텀 생성자: 일부 필드 기본값 적용
  public CursorPageRequestDepartmentDto(
      String nameOrDescription,
      String cursor,
      Long idAfter,
      Direction sortDirection,
      String sortField,
      Integer size
  ) {
    this.nameOrDescription = nameOrDescription;
    this.cursor = cursor;
    this.idAfter = idAfter;
    this.sortDirection = sortDirection == null ? Direction.ASC : sortDirection; // 기본 DESC
    this.sortField = sortField == null ? "establishedDate" : sortField;         // 기본 establishedDate
    this.size = size == null ? 10 : size;                                        // 기본 20
  }
}
