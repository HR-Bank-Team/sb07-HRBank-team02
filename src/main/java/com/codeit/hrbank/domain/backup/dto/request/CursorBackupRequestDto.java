package com.codeit.hrbank.domain.backup.dto.request;

import com.codeit.hrbank.domain.backup.entity.BackupStatus;

import java.time.Instant;
import java.time.LocalDateTime;

public record CursorBackupRequestDto(
        String worker,
        BackupStatus status,
        Instant startedAtFrom,
        Instant startedAtTo,
        String sortField,
        String sortDirection,
        int size,
        LocalDateTime cursor,
        Long idAfter
) {
}
