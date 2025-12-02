package com.codeit.hrbank.domain.changelog.repository;

import com.codeit.hrbank.domain.changelog.entity.ChangeLog;
import com.codeit.hrbank.domain.changelog.entity.ChangeLogType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public interface ChangeLogRepository extends JpaRepository<ChangeLog, Long>, ChangeLogCustomRepository {

    @Query("SELECT max(c.at) FROM ChangeLog c")
    LocalDateTime getLatestChangeTime();

}
