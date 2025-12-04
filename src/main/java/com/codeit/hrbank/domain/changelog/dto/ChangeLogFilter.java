package com.codeit.hrbank.domain.changelog.dto;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;


@Getter
@AllArgsConstructor
public class ChangeLogFilter {

    private String employeeNumber;
    private String type;
    private String memo;
    private String ipAddress;

    @NotNull
    private String sortField;
    @NotNull
    private String sortDirection;
    @NotNull
    private Integer size;

    private OffsetDateTime atFrom;
    private OffsetDateTime atTo;

    private String cursor;
    private Long idAfter;

}
