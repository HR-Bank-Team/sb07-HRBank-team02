package com.codeit.hrbank.domain.backup.repository;

import com.codeit.hrbank.domain.backup.entity.Backup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface BackupRepository extends JpaRepository<Backup, Long> {

    @Query("select bu from Backup bu where bu.endedAt = (select  max(su.endedAt) from Backup su)")
    Backup getLatestBackup();
}