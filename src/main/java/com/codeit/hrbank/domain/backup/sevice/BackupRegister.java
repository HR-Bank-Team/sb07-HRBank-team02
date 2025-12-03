package com.codeit.hrbank.domain.backup.sevice;

import com.codeit.hrbank.domain.backup.dto.export.ExportEmployeeDto;
import com.codeit.hrbank.domain.backup.dto.response.BackupDto;
import com.codeit.hrbank.domain.backup.entity.Backup;
import com.codeit.hrbank.domain.backup.entity.BackupStatus;
import com.codeit.hrbank.domain.backup.mapper.BackupMapper;
import com.codeit.hrbank.domain.backup.mapper.ExportEmployeeMapper;
import com.codeit.hrbank.domain.backup.repository.BackupRepository;
import com.codeit.hrbank.domain.changelog.repository.ChangeLogRepository;
import com.codeit.hrbank.domain.employee.repository.EmployeeRepository;
import com.codeit.hrbank.domain.file.entity.File;
import com.codeit.hrbank.domain.file.repository.FileRepository;
import com.codeit.hrbank.domain.file.service.FileStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BackupRegister {

    private final BackupRepository backupRepository;
    private final FileStorage fileStorage;
    private final FileRepository fileRepository;
    private final EmployeeRepository employeeRepository;
    private final ExportEmployeeMapper exportEmployeeMapper;
    private final ChangeLogRepository changeLogRepository;
    private final BackupMapper backupMapper;

    static final String SYSTEM_USER = "SYSTEM";
    private LocalDateTime latestBackupTime = LocalDateTime.MIN;

    @Transactional
    public BackupDto createBackup(String ip) {
        LocalDateTime latestChangeTime = changeLogRepository.getLatestChangeTime();
        latestChangeTime = (latestChangeTime == null) ? LocalDateTime.now() : latestChangeTime;

        if(!isNecessaryBackup(latestChangeTime)){
            Backup backup = backupRepository.save(
                    new Backup(ip, LocalDateTime.now(), LocalDateTime.now(), BackupStatus.SKIPPED, null));
            return backupMapper.toDto(backup);
        }

        Backup backup = saveBackup(ip);
        latestBackupTime = backup.getEndedAt();
        return backupMapper.toDto(backup);
    }

    @Transactional
    public BackupDto createBatchBackup(){
        return createBackup(SYSTEM_USER);
    }

    @Transactional
    protected Backup saveBackup(String ip)  {

        Backup backup = new Backup(ip,LocalDateTime.now(),LocalDateTime.now(),BackupStatus.IN_PROGRESS,null);
        String fileName = backup.getStartedAt().toString().replace(":", "-") + ".csv";
        List<ExportEmployeeDto> employeeDtos = employeeRepository.findAll().stream().map(exportEmployeeMapper::toDto).toList();

        try {
            Long fileSize = fileStorage.saveCsv(fileName, employeeDtos);
            File file = fileRepository.save(new File(fileName, "text/csv", fileSize));
            backup.backupComplete(file);
        }
        catch (Exception e) {
            String errorLogName = backup.getStartedAt().toString().replace(":", "-")+".log";
            try {
                Long fileSize = fileStorage.saveLog(errorLogName, e.getMessage());
                File file =  fileRepository.save(new File(fileName, "text/plain", fileSize));
                backup.backupFail(file);
            }
            catch (IOException ioException) {
                ioException.printStackTrace();
            }

        }
        return backupRepository.save(backup);
    }

    private boolean isNecessaryBackup(LocalDateTime changeLogTime) {
        return changeLogTime.isAfter(latestBackupTime);
    }

}
