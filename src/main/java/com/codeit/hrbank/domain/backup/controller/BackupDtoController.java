package com.codeit.hrbank.domain.backup.controller;

import com.codeit.hrbank.domain.backup.dto.request.BackupRequestDto;
import com.codeit.hrbank.domain.backup.sevice.BackupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/backups")
public class BackupDtoController {

    private final BackupService backupService;

    @GetMapping("")
    public void getBackupPage(@RequestBody BackupRequestDto backupRequestDto){

    }
}
