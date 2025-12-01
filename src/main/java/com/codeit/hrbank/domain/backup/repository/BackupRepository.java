package com.codeit.hrbank.domain.backup.repository;

import com.codeit.hrbank.domain.backup.entity.Backup;
import com.codeit.hrbank.domain.backup.entity.BackupStatus;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

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
    select bu from Backup bu where (?1 is null or bu.worker like %?1%)
    and (?2 is null or bu.status = ?2)
    and (?3 is null or bu.endedAt between ?3 and ?4)
    order by 
        case when ?5 = 'asc' then bu.endedAt end asc,
        case when ?5 = 'desc' then bu.endedAt end desc
    
""")
    Slice<Backup> getBackupSlice(String worker, BackupStatus status, LocalDateTime startedAtFrom,
                                 LocalDateTime startedAtTo,String sortDirection,
                                 String sortField, Long size);
}