package com.codeit.hrbank.domain.backup.entity;

public enum BackupStatus {
    IN_PROGRESS("진행 중"),
    COMPLETED("완료"),
    FAILED("실패"),
    SKIPPED("건너뜀");

    private final String desc;

    BackupStatus(String desc) {
        this.desc = desc;
    }
}
