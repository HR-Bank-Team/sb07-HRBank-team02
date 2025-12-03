package com.codeit.hrbank.domain.changelog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
public class ChangeLogDto {
    private Long id;
    private String type;
    private String employeeNumber;
    private String memo;
    private String ipAddress;
    private String at;
}
