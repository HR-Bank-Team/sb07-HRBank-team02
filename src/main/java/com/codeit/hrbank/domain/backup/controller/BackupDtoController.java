package com.codeit.hrbank.domain.backup.controller;

import com.codeit.hrbank.domain.backup.dto.request.BackupRequestDto;
import com.codeit.hrbank.domain.backup.dto.response.BackupDto;
import com.codeit.hrbank.domain.backup.dto.response.CursorPageResponseBackupDto;
import com.codeit.hrbank.domain.backup.sevice.BackupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/backups")
public class BackupDtoController {

    private final BackupService backupService;

    @GetMapping("")
    public ResponseEntity<CursorPageResponseBackupDto> getBackupPage(@RequestBody BackupRequestDto backupRequestDto){
        return null;
    }

    @PostMapping("")
    public ResponseEntity<BackupDto> createBackup(){
        return null;
    }

        @GetMapping("/latest")
    public ResponseEntity<BackupDto> getLatestBackup(){
        return null;
        }
}
