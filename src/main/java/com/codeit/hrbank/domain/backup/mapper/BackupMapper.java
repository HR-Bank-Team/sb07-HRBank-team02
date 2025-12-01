package com.codeit.hrbank.domain.backup.mapper;

import com.codeit.hrbank.domain.backup.dto.response.BackupDto;
import com.codeit.hrbank.domain.backup.entity.Backup;
import org.springframework.stereotype.Service;

@Service
public class BackupMapper {

    public BackupDto toDto(Backup backup){
        return new BackupDto(
                backup.getId(),
                backup.getWorker(),
                backup.getStartedAt(),
                backup.getEndedAt(),
                backup.getStatus(),
                backup.getFile().getId()
        );
    }
}
