package com.codeit.hrbank.domain.department.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum SortField {
    name,
    establishedDate;

    @JsonCreator
    public static SortField from(String value) {
        if (value == null || value.isBlank()) return establishedDate;
        try {
            return SortField.valueOf(value);
        } catch (Exception e) {
            return establishedDate;
        }
    }
}
