package com.codeit.hrbank.domain.backup.dto.response;

import com.codeit.hrbank.domain.backup.entity.BackupStatus;

import java.time.LocalDateTime;

public record BackupDto(
        Long id,
        String worker,
        LocalDateTime startedAt,
        LocalDateTime endedAt,
        BackupStatus status,
        Long fileId
) {
}
