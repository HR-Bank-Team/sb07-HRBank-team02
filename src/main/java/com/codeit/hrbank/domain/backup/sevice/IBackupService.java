package com.codeit.hrbank.domain.backup.sevice;

import com.codeit.hrbank.domain.backup.dto.request.CursorBackupRequestDto;
import com.codeit.hrbank.domain.backup.dto.response.BackupDto;
import com.codeit.hrbank.domain.backup.dto.response.CursorPageResponseBackupDto;
import com.codeit.hrbank.domain.backup.entity.Backup;
import com.codeit.hrbank.domain.backup.entity.BackupStatus;
import com.codeit.hrbank.domain.backup.mapper.BackupMapper;
import com.codeit.hrbank.domain.backup.mapper.CursorPageBackupMapper;
import com.codeit.hrbank.domain.backup.repository.BackupRepository;

import com.codeit.hrbank.domain.changelog.repository.ChangeLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;


@Service
@RequiredArgsConstructor
public class IBackupService implements BackupService{

    private final BackupRepository backupRepository;
    private final BackupScheduler backupScheduler;
    private final ChangeLogRepository changeLogRepository;
    private final BackupMapper backupMapper;
    private final BackupRegister backupRegister;
    private final CursorPageBackupMapper cursorPageBackupMapper;

    @Override
    public CursorPageResponseBackupDto getBackupList(CursorBackupRequestDto cursorBackupRequestDto) {

        String worker = cursorBackupRequestDto.worker();
        LocalDateTime start = cursorBackupRequestDto.startedAtFrom();
        LocalDateTime end = cursorBackupRequestDto.startedAtTo();
        BackupStatus status = cursorBackupRequestDto.status();
        String sortField = cursorBackupRequestDto.sortField();
        String sortDirection = cursorBackupRequestDto.sortDirection();
        Long size = cursorBackupRequestDto.size();

        Slice<Backup> backupSlice = backupRepository.getBackupSlice(worker, status, start,
                end, sortDirection, sortField, size);

        Object nextCursor =  backupSlice.isEmpty()
                ? LocalDateTime.now()
                : backupSlice.getContent().
                get(backupSlice.getContent().size()-1).getStartedAt();;

        if(sortField.equalsIgnoreCase("startedAt")) nextCursor =
                backupSlice.isEmpty()
                        ? LocalDateTime.now()
                        : backupSlice.getContent().
                        get(backupSlice.getContent().size()-1).getStartedAt();

        if(sortField.equalsIgnoreCase("endedAt")) nextCursor =
                backupSlice.isEmpty()
                        ? LocalDateTime.now()
                        : backupSlice.getContent().
                        get(backupSlice.getContent().size()-1).getEndedAt();

        return cursorPageBackupMapper.toDto(backupSlice,nextCursor);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public BackupDto createBackup(HttpServletRequest request) throws Exception {
        String ip = request.getRemoteAddr();
        LocalDateTime latestChangeTime = changeLogRepository.getLatestChangeTime();
        latestChangeTime = (latestChangeTime == null) ? LocalDateTime.now() : latestChangeTime;

        if(!isNecessaryBackup(latestChangeTime)){
            Backup backup = backupRepository.save(
                    new Backup(ip, LocalDateTime.now(), LocalDateTime.now(), BackupStatus.SKIPPED, null));
            return backupMapper.toDto(backup);
        }
        Backup backup = backupRepository.save(
                new Backup(ip,LocalDateTime.now(),null,BackupStatus.IN_PROGRESS,null)
        );
        Long backupId = backup.getId();
        backupRegister.afterRegister(backup);
        backup = backupRepository.findById(backupId).orElseThrow();
        return backupMapper.toDto(backup);
    }

    @Override
    public BackupDto getLatestBackup() {
    Backup latestBackup = backupRepository.getLatestBackup();
    return backupMapper.toDto(latestBackup);
    }
    private boolean isNecessaryBackup(LocalDateTime changeLogTime) {
        return changeLogTime.isAfter(backupScheduler.getLatestBackupTime());
    }

    private List<Backup> retainCommon(List<Backup> backupList1, List<Backup> backupList2) {
        if(backupList1== null && backupList2 == null){
            return null;
        }
        if(backupList1 == null){
            return backupList2;
        }
        if(backupList2 == null){
            return backupList1;
        }
        Set<Backup> backupSet = new HashSet<>(backupList1);
        backupSet.retainAll(backupList2);
        return backupSet.stream().toList();
    }





}
