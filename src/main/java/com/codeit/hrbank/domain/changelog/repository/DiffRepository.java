package com.codeit.hrbank.domain.changelog.repository;

import com.codeit.hrbank.domain.changelog.entity.ChangeLog;
import com.codeit.hrbank.domain.changelog.entity.Diff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiffRepository extends JpaRepository<Diff, Long> {
    List<Diff> findByChangeLogId(Long changeLogId);
}
