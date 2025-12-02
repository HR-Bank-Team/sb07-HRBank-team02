package com.codeit.hrbank.domain.changelog.mapper;

import com.codeit.hrbank.domain.changelog.dto.DiffDto;
import com.codeit.hrbank.domain.changelog.entity.Diff;

public class DiffMapper {

    public static DiffDto toDto(Diff diff){
        return new DiffDto(diff.getPropertyName(), diff.getBefore(), diff.getAfter());
    }
}
