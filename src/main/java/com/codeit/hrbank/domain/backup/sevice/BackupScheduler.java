package com.codeit.hrbank.domain.backup.sevice;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BackupScheduler {

    private LocalDateTime lastBackupTime;

    public LocalDateTime getRecentBackupTime() {
        return lastBackupTime;
    }
}
