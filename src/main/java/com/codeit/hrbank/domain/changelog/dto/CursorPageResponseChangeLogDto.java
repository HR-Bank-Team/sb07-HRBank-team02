package com.codeit.hrbank.domain.changelog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@AllArgsConstructor
public class CursorPageResponseChangeLogDto {
    private List<ChangeLogDto> content;
    private String nextCursor;
    private Long nextIdAfter;
    private Integer size;
    private Long totalElements;
    private boolean hasNext;

}
