package com.codeit.hrbank.domain.backup.sevice;

import com.codeit.hrbank.domain.backup.dto.request.CursorBackupRequestDto;
import com.codeit.hrbank.domain.backup.dto.response.BackupDto;
import com.codeit.hrbank.domain.backup.dto.response.CursorPageResponseBackupDto;
import com.codeit.hrbank.domain.backup.entity.Backup;
import com.codeit.hrbank.domain.backup.entity.BackupStatus;
import com.codeit.hrbank.domain.backup.mapper.BackupMapper;
import com.codeit.hrbank.domain.backup.mapper.CursorPageBackupMapper;
import com.codeit.hrbank.domain.backup.repository.BackupRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Slf4j
@Service
@RequiredArgsConstructor
public class IBackupService implements BackupService{

    private final BackupRepository backupRepository;
    private final BackupMapper backupMapper;
    private final BackupRegister backupRegister;
    private final CursorPageBackupMapper cursorPageBackupMapper;

    @Override
    @Transactional(readOnly = true)
    public CursorPageResponseBackupDto getBackupList(CursorBackupRequestDto cursorBackupRequestDto) {


        String worker = cursorBackupRequestDto.worker();
        LocalDateTime start = cursorBackupRequestDto.startedAtFrom();
        LocalDateTime end = cursorBackupRequestDto.startedAtTo();
        BackupStatus status = cursorBackupRequestDto.status();
        String sortField = cursorBackupRequestDto.sortField();
        String sortDirection = cursorBackupRequestDto.sortDirection();
        int size = cursorBackupRequestDto.size();

        String safeSortField = (sortField == null || sortField.isBlank()) ? "endedAt" : sortField;
        Sort.Direction direction = "DESC".equalsIgnoreCase(sortDirection) ? Sort.Direction.DESC : Sort.Direction.ASC;

        Sort sort = Sort.by(direction ,safeSortField);
        Pageable pageable = PageRequest.of( 0,size
        , sort
        );
        Slice<Backup> backupSlice = backupRepository.getBackupSlice(worker, status, start,
                end,pageable);
        log.info("backupSlice : {}",backupSlice);
        LocalDateTime nextCursor =  backupSlice.isEmpty()
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
    public BackupDto createBackup(HttpServletRequest request) throws Exception {
        String ip = request.getRemoteAddr();
        return backupRegister.createBackup(ip);
    }

    @Override
    @Transactional(readOnly = true)
    public BackupDto getLatestBackup() {
    Backup latestBackup = backupRepository.getLatestBackup();
    return backupMapper.toDto(latestBackup);
    }

}
