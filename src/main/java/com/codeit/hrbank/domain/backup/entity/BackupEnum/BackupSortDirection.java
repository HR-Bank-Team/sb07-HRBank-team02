package com.codeit.hrbank.domain.backup.entity.BackupEnum;

import lombok.Getter;

@Getter
public enum BackupSortDirection {

    ASC("asc"), DESC("desc");
    private final String value;
    BackupSortDirection(String value) {
        this.value = value;
    }

}
