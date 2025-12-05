package com.codeit.hrbank.domain.department.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.data.domain.Sort.Direction;

public enum SortDirection {
    ASC,
    DESC;

    @JsonCreator
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

    @JsonValue
    public String toJson() {
        // NOTE: 응답에 "asc"/"desc" 소문자로 내려가게 하고 싶다면, 필요없을시 해당 메서드 지워도됨
        return this.name().toLowerCase();
    }

    public Direction toSpringDirection() {
        return (this == ASC) ? Direction.ASC : Direction.DESC;
    }
}
