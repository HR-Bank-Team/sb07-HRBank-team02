package com.codeit.hrbank.domain.backup.sevice;

import com.codeit.hrbank.domain.backup.dto.request.CursorBackupRequestDto;
import com.codeit.hrbank.domain.backup.dto.response.BackupDto;
import com.codeit.hrbank.domain.backup.dto.response.CursorPageResponseBackupDto;
import com.codeit.hrbank.domain.backup.entity.Backup;
import com.codeit.hrbank.domain.backup.entity.BackupStatus;
import com.codeit.hrbank.domain.backup.mapper.BackupMapper;
import com.codeit.hrbank.domain.backup.mapper.CursorPageBackupMapper;
import com.codeit.hrbank.domain.backup.repository.BackupRepository;
import com.codeit.hrbank.domain.backup.repository.BackSliceRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class IBackupService implements BackupService{

    private final BackupRepository backupRepository;
    private final BackupMapper backupMapper;
    private final BackupRegister backupRegister;
    private final CursorPageBackupMapper cursorPageBackupMapper;
    private final BackSliceRepository backSliceRepository;


    @Override
    @Transactional(readOnly = true)
    public CursorPageResponseBackupDto getBackupList(CursorBackupRequestDto cursorBackupRequestDto) {

        LocalDateTime start = cursorBackupRequestDto.startedAtFrom()==null
                ?null
                :cursorBackupRequestDto.startedAtFrom().atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime();

        LocalDateTime end = cursorBackupRequestDto.startedAtTo()==null
                ?null
                :cursorBackupRequestDto.startedAtTo().atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime();
        BackupStatus status = cursorBackupRequestDto.status();
        int size = cursorBackupRequestDto.size();

        var backupList = backSliceRepository.getBackupSlice(
                cursorBackupRequestDto.worker(),
                status,
                start,
                end,
                cursorBackupRequestDto.cursor(),
                cursorBackupRequestDto.idAfter(),
                cursorBackupRequestDto.sortField(),
                cursorBackupRequestDto.sortDirection(),
                size+1
        );
        List<BackupDto> backupDtoList = backupList.stream().map(backupMapper::toDto).toList();

        Long totalElement = backSliceRepository.countBackup(
                cursorBackupRequestDto.worker(),
                status,
                start,
                end,
                cursorBackupRequestDto.cursor(),
                cursorBackupRequestDto.idAfter(),
                cursorBackupRequestDto.sortField(),
                cursorBackupRequestDto.sortDirection()
        );

        return cursorPageBackupMapper.toDto(backupDtoList,totalElement,size,cursorBackupRequestDto.sortField());
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
