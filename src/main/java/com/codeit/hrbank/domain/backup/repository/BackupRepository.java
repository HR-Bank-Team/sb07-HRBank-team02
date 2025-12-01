package com.codeit.hrbank.domain.backup.repository;

import com.codeit.hrbank.domain.backup.entity.Backup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BackupRepository extends JpaRepository<Backup, Long> {

}
