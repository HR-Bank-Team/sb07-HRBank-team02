package com.codeit.hrbank.domain.backup.sevice;

import com.codeit.hrbank.domain.backup.dto.request.CursorBackupRequestDto;
import com.codeit.hrbank.domain.backup.dto.response.BackupDto;
import com.codeit.hrbank.domain.backup.dto.response.CursorPageResponseBackupDto;
import com.codeit.hrbank.domain.backup.repository.BackupRepository;
import com.codeit.hrbank.domain.changelog.entity.ChangeLog;

import com.codeit.hrbank.domain.changelog.repository.ChangeLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BackupServicei implements BackupService{


    private final BackupRepository backupRepository;
    private final BackupScheduler backupScheduler;
    private final ChangeLogRepository changeLogRepository;


    @Override
    public CursorPageResponseBackupDto getBackupList(CursorBackupRequestDto cursorBackupRequestDto) {
        return null;
    }

    @Override
    public BackupDto createBackup(HttpServletRequest request) {
        return null;
    }
    @Override
    public BackupDto getLatestBackup() {
        return null;
    }
    private boolean isNecessaryBackup(LocalDateTime changeLogTime) {
        return changeLogTime.isAfter(backupScheduler.getLatestBackupTime());
    }

}
