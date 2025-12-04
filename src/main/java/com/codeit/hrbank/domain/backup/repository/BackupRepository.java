package com.codeit.hrbank.domain.backup.repository;

import com.codeit.hrbank.domain.backup.entity.Backup;
import com.codeit.hrbank.domain.backup.entity.BackupStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;


public interface BackupRepository extends JpaRepository<Backup, Long> {

    @Query("select bu from Backup bu where bu.endedAt = (select  max(su.endedAt) from Backup su)")
    Backup getLatestBackup();

    @Query("select bu from Backup bu where bu.worker Like %?1% ")
    List<Backup> getBackupsWithWorker(String worker);

    @Query("select bu from Backup bu where bu.status = ?1")
    List<Backup> getBackupsByStatus(BackupStatus status);

    @Query("select bu from Backup bu where bu.createdAt between ?1 and ?2")
    List<Backup> getBackupByTime(LocalDateTime startedAtFrom,LocalDateTime startedAtTo);

    @Query("""
    select bu from Backup bu where (:worker is null or bu.worker like %:worker%)
    and (:status is null or bu.status = :status)
    and (:startedAtFrom is null or bu.endedAt between :startedAtFrom and :startedAtTo)
""")
    Slice<Backup> getBackupSlice(
            @Param("worker") String worker,
            @Param("status") BackupStatus status,
            @Param("startedAtFrom") LocalDateTime startedAtFrom,
            @Param("startedAtTo") LocalDateTime startedAtTo,
            Pageable pageable);
}