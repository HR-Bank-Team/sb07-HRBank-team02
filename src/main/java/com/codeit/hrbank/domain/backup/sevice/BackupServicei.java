package com.codeit.hrbank.domain.backup.sevice;

import com.codeit.hrbank.domain.changelog.entity.ChangeLog;

public class BackupServicei implements BackupService{

    @Override
    public boolean isNecessaryBackup(ChangeLog changeLog) {
        return false;
    }

    @Override
    public void registerBackup(ChangeLog changeLog) {

    }
}
