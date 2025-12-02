package com.codeit.hrbank.domain.changelog.mapper;


import com.codeit.hrbank.domain.changelog.dto.ChangeLogDto;
import com.codeit.hrbank.domain.changelog.entity.ChangeLog;

public class ChangeLogMapper {

    public static ChangeLogDto toDto(ChangeLog changeLog){
        return new ChangeLogDto(
                changeLog.getId(),
                changeLog.getType().toString(),
                changeLog.getEmployeeNumber(),
                changeLog.getMemo(),
                changeLog.getIpAddress(),
                changeLog.getAt().toString());
    }
}
