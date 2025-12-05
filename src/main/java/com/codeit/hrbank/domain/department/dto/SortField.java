package com.codeit.hrbank.domain.department.dto;

public enum SortField {
    name,
    establishedDate;

    public static SortField from(String value) {
        if (value == null || value.isBlank()) return establishedDate;
        try {
            return SortField.valueOf(value);
        } catch (Exception e) {
            return establishedDate;
        }
    }
}
