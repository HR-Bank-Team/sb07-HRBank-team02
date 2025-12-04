package com.codeit.hrbank.domain.backup.controller;

import com.codeit.hrbank.domain.backup.controller.docs.BackupControllerDocs;
import com.codeit.hrbank.domain.backup.dto.request.CursorBackupRequestDto;
import com.codeit.hrbank.domain.backup.dto.response.BackupDto;
import com.codeit.hrbank.domain.backup.dto.response.CursorPageResponseBackupDto;
import com.codeit.hrbank.domain.backup.entity.BackupStatus;
import com.codeit.hrbank.domain.backup.sevice.BackupService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/backups")
public class BackupController implements BackupControllerDocs {

    private final BackupService backupService;

    @GetMapping("")
    @Override
    public ResponseEntity<CursorPageResponseBackupDto> getBackupPage(@RequestParam(required = false) String worker
            , @RequestParam(required = false) BackupStatus status, @RequestParam(required = false) LocalDateTime startedAtFrom,
                                                                     @RequestParam(required = false) LocalDateTime startedAtTo,
                                                                     @RequestParam(required = false) String sortDirection,
                                                                     @RequestParam(required = false) String sortField,
                                                                     @RequestParam(required = false) int size
    ) {
        CursorBackupRequestDto dto = new CursorBackupRequestDto(
                worker, status, startedAtFrom, startedAtTo, sortField, sortDirection, size
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
