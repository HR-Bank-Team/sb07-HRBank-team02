package com.codeit.hrbank.domain.department.service;

public record NextCursorInfo(
        String nextCursor,
        Long nextIdAfter
) {
}
