package com.codeit.hrbank.domain.backup.sevice;

import com.codeit.hrbank.domain.backup.dto.request.BackupRequestDto;
import com.codeit.hrbank.domain.backup.dto.response.BackupDto;
import com.codeit.hrbank.domain.backup.dto.response.CursorPageResponseBackupDto;
import com.codeit.hrbank.domain.changelog.entity.ChangeLog;

public interface BackupService {

CursorPageResponseBackupDto getBackupList(BackupRequestDto backupRequestDto);
BackupDto createBackup();
BackupDto getLatestBackup();



}
