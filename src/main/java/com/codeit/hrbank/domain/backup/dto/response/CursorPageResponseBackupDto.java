package com.codeit.hrbank.domain.backup.dto.response;

import java.time.LocalDateTime;

public record CursorPageResponseBackupDto(

        Object[] content,
        LocalDateTime nextCursor,
        Long nextIdAfter,
        Long size,
        Long totalElements,
        boolean hasNext
) {
}
