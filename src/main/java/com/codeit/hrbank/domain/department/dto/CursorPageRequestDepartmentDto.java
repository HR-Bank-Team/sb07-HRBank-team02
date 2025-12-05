package com.codeit.hrbank.domain.department.dto;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

public record CursorPageRequestDepartmentDto(
        String nameOrDescription,  // 검색 키워드
        String cursor,             // 정렬 컬럼 마지막 값
        Long idAfter,              // 마지막 id
        SortDirection sortDirection, //desc or asc -> DESC or ASC 값으로 변경
        SortField sortField,          // 정렬 필드 name or establishedDate
        Integer size
) {
    public Direction sortDirectionOrDefault() {
        // null이면 Enum에서 기본값(DESC) 쓰게끔
        SortDirection sortDirectionDefault = (sortDirection == null) ? SortDirection.DESC : sortDirection;
        return sortDirectionDefault.toSpringDirection();
    }



    public SortField sortFieldOrDefault() {
        return (sortField == null) ? SortField.establishedDate : sortField;
    }

    public int sizeOrDefault() {
        return (size == null || size <= 0) ? 10 : size;
    }

    public String toDirectionString() {
        return sortDirectionOrDefault().name().toLowerCase();
    }

    // JPQL에서 사용할 문자열 ( 'name' / 'establishedDate' )
    public String toSortFieldString() {
        return sortFieldOrDefault().name();   // enum 이름 그대로: "name", "establishedDate"
    }


    public Pageable toPageable() {
        Direction direction = sortDirectionOrDefault();
        Sort sort = Sort.by(direction, toSortFieldString())
                .and(Sort.by(direction, "id"));
        return PageRequest.of(0, sizeOrDefault(), sort);
    }
}
