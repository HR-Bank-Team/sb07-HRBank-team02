package com.codeit.hrbank.domain.backup.sevice;

import com.codeit.hrbank.domain.backup.repository.BackupRepository;
import com.codeit.hrbank.domain.changelog.entity.ChangeLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BackupServicei implements BackupService{


    private final BackupRepository backupRepository;
    private final BackupScheduler backupScheduler;

    private boolean isNecessaryBackup(ChangeLog changeLog) {
        return changeLog.getAt().isAfter(backupScheduler.getRecentBackupTime());
    }




    private void registerBackup(ChangeLog changeLog) {

    }
}
