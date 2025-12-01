package com.codeit.hrbank.domain.backup.dto.response;

public record CursorPageResponseBackupDto(

        Object[] content,
        String nextCursor,
        Long nextIdAfter,
        Long size,
        Long totalElements,
        boolean hasNext
) {
}
