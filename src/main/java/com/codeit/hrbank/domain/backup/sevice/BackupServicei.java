package com.codeit.hrbank.domain.backup.sevice;

import com.codeit.hrbank.domain.backup.dto.request.BackupRequestDto;
import com.codeit.hrbank.domain.backup.dto.response.BackupDto;
import com.codeit.hrbank.domain.backup.dto.response.CursorPageResponseBackupDto;
import com.codeit.hrbank.domain.backup.repository.BackupRepository;
import com.codeit.hrbank.domain.changelog.entity.ChangeLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BackupServicei implements BackupService{


    private final BackupRepository backupRepository;
    private final BackupScheduler backupScheduler;


    @Override
    public CursorPageResponseBackupDto getBackupList(BackupRequestDto backupRequestDto) {
        return null;
    }

    @Override
    public BackupDto createBackup() {
        return null;
    }

    @Override
    public BackupDto getLatestBackup() {
        return null;
    }

    private boolean isNecessaryBackup(ChangeLog changeLog) {
        return changeLog.getAt().isAfter(backupScheduler.getRecentBackupTime());
    }


    private void registerBackup(ChangeLog changeLog) {

    }
}
