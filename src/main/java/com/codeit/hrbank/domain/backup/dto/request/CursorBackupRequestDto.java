package com.codeit.hrbank.domain.backup.dto.request;

import com.codeit.hrbank.domain.backup.entity.BackupSortField;
import com.codeit.hrbank.domain.backup.entity.BackupStatus;
import com.codeit.hrbank.domain.backup.entity.BackupSortDirection;

import java.time.Instant;
import java.time.LocalDateTime;

public record CursorBackupRequestDto(
        String worker,
        BackupStatus status,
        Instant startedAtFrom,
        Instant startedAtTo,
        BackupSortField sortField,
        BackupSortDirection sortDirection,
        int size,
        LocalDateTime cursor,
        Long idAfter
) {
}
