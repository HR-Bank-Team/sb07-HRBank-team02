package com.codeit.hrbank.domain.backup.sevice;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class BackupScheduler {

    private final BackupRegister backupRegister;
    static final long BATCH_INTERVAL = 1000 * 60*60; //10분마다 호출함

    @Scheduled(fixedRate =BATCH_INTERVAL)
    public void createBackup() throws Exception {
        log.info("Backup Scheduler time: {}",LocalDateTime.now());
        backupRegister.createBatchBackup();
    }
}
