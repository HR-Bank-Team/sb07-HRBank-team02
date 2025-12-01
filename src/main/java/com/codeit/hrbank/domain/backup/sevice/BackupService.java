package com.codeit.hrbank.domain.backup.sevice;

import com.codeit.hrbank.domain.backup.dto.request.CursorBackupRequestDto;
import com.codeit.hrbank.domain.backup.dto.response.BackupDto;
import com.codeit.hrbank.domain.backup.dto.response.CursorPageResponseBackupDto;
import jakarta.servlet.http.HttpServletRequest;

public interface BackupService {

    CursorPageResponseBackupDto getBackupList(CursorBackupRequestDto cursorBackupRequestDto);
    BackupDto createBackup(HttpServletRequest request) throws Exception;
    BackupDto getLatestBackup();

}
