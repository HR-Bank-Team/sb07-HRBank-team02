package com.codeit.hrbank.domain.changelog.repository;

import com.codeit.hrbank.domain.changelog.entity.ChangeLog;

import java.time.LocalDateTime;
import java.util.List;

public interface ChangeLogCustomRepository {

    List<ChangeLog> searchChangeLogs(
            String employeeNumber,
            String memo,
            String ipAddress,
            LocalDateTime atFrom,
            LocalDateTime atTo,
            String type,
            String cursor,
            String sortField,
            String sortDirection,
            Long idAfter,
            Integer limit
    );

    Long countChangeLogs(
            String employeeNumber,
            String memo,
            String ipAddress,
            LocalDateTime atFrom,
            LocalDateTime atTo,
            String type
    );
}
