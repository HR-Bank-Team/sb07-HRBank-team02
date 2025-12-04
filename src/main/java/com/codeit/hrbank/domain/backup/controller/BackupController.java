package com.codeit.hrbank.domain.backup.controller;

import com.codeit.hrbank.domain.backup.controller.docs.BackupControllerDocs;
import com.codeit.hrbank.domain.backup.dto.request.CursorBackupRequestDto;
import com.codeit.hrbank.domain.backup.dto.response.BackupDto;
import com.codeit.hrbank.domain.backup.dto.response.CursorPageResponseBackupDto;
import com.codeit.hrbank.domain.backup.entity.BackupSortField;
import com.codeit.hrbank.domain.backup.entity.BackupStatus;
import com.codeit.hrbank.domain.backup.entity.BackupSortDirection;
import com.codeit.hrbank.domain.backup.sevice.BackupService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/backups")
public class BackupController implements BackupControllerDocs {

    private final BackupService backupService;

    @GetMapping("")
    @Override
    public ResponseEntity<CursorPageResponseBackupDto> getBackupPage(
            @RequestParam(required = false) String worker,
            @RequestParam(required = false) BackupStatus status,
            @RequestParam(required = false) Instant startedAtFrom,
            @RequestParam(required = false) Instant startedAtTo,
            @RequestParam(required = false) BackupSortDirection sortDirection,
            @RequestParam(required = false) BackupSortField sortField,
            @RequestParam(required = false) int size,
            @RequestParam(required = false) LocalDateTime cursor,
            @RequestParam(required = false) Long idAfter
    ) {
        CursorBackupRequestDto dto =
                new CursorBackupRequestDto(
                worker,
                status,
                startedAtFrom,
                startedAtTo,
                sortField,
                sortDirection,
                size,
                cursor,
                idAfter
        );

        CursorPageResponseBackupDto backupList = backupService.getBackupList(dto);
        return new ResponseEntity<>(backupList, HttpStatus.OK);
    }


    @PostMapping("")
    @Override
    public ResponseEntity<BackupDto> createBackup(HttpServletRequest request) throws Exception {
        BackupDto backup = backupService.createBackup(request);
        return new ResponseEntity<>(backup, HttpStatus.CREATED);
    }

    @GetMapping("/latest")
    @Override
    public ResponseEntity<BackupDto> getLatestBackup() {
        BackupDto backup = backupService.getLatestBackup();
        return new ResponseEntity<>(backup, HttpStatus.OK);
    }
}
