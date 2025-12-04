package com.codeit.hrbank.domain.backup.entity;

import lombok.Getter;

@Getter
public enum BackupSortField {

    STARTED_AT("started_at"),ENDED_AT("ended_at");
    private final String value;
    BackupSortField(String value) {
        this.value = value;
    }

}
