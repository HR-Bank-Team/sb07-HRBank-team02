package com.codeit.hrbank.domain.backup.dto.request;

import com.codeit.hrbank.domain.backup.entity.BackupStatus;

import java.time.LocalDateTime;

public record CursorBackupRequestDto(
        String worker,
        BackupStatus status,
        LocalDateTime startedAtFrom,
        LocalDateTime startedAtTo,
        Long idAfter,
        Long cursor,
        Long size,
        String sortField,
        String sortDirection
) {
}
