package com.codeit.hrbank.domain.backup.controller;

import com.codeit.hrbank.domain.backup.dto.request.CursorBackupRequestDto;
import com.codeit.hrbank.domain.backup.dto.response.BackupDto;
import com.codeit.hrbank.domain.backup.dto.response.CursorPageResponseBackupDto;
import com.codeit.hrbank.domain.backup.sevice.BackupService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/backups")
public class BackupController {

    private final BackupService backupService;

    @GetMapping("")
    public ResponseEntity<CursorPageResponseBackupDto> getBackupPage(@Valid @RequestBody CursorBackupRequestDto cursorBackupRequestDto){
        return null;
    }

    @PostMapping("")
    public ResponseEntity<BackupDto> createBackup(HttpServletRequest request){
        BackupDto backup = backupService.createBackup(request);
        return new ResponseEntity<>(backup, HttpStatus.CREATED);
    }

        @GetMapping("/latest")
    public ResponseEntity<BackupDto> getLatestBackup(){
        BackupDto backup = backupService.getLatestBackup();
        return new ResponseEntity<>(backup, HttpStatus.OK);
        }
}
