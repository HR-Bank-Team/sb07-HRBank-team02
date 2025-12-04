package com.codeit.hrbank.domain.changelog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DiffCommand {
    private String propertyName;
    private String before;
    private String after;
}
