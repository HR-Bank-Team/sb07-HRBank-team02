package com.codeit.hrbank.domain.changelog.dto;

import java.util.List;

public class CursorPageResponseChangeLogDto {
    private List<ChangeLogDto> content;
    private String nextCursor;
    private Long nextIdAfter;
    private Long size;
    private Long totalElements;
    private boolean hasNext;
}
