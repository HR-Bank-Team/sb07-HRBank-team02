package com.codeit.hrbank.domain.department.dto;

import org.springframework.data.domain.Sort.Direction;

public enum SortDirection {
    ASC,
    DESC;

    public static SortDirection from(String value) {
        if (value == null || value.isBlank()) {
            return DESC; // 기본값
        }
        try {
            return SortDirection.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            // 잘못된 값 들어오면 기본값으로
            return DESC;
        }
    }

    public Direction toSpringDirection() {
        return (this == ASC) ? Direction.ASC : Direction.DESC;
    }
}
