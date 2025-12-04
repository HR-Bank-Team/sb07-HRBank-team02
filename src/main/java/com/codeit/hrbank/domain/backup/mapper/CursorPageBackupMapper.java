package com.codeit.hrbank.domain.backup.mapper;

import com.codeit.hrbank.domain.backup.dto.response.BackupDto;
import com.codeit.hrbank.domain.backup.dto.response.CursorPageResponseBackupDto;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class CursorPageBackupMapper {

    public CursorPageResponseBackupDto toDto(List<BackupDto> backupDtos,long totalElements,int size,String sortField){
        boolean hasNext = backupDtos.size() > size;
        int endIndex = Math.min(backupDtos.size(), size);
        List<BackupDto> content = backupDtos.subList(0, endIndex);
        BackupDto lastBackupDto = content.isEmpty()?null: backupDtos.get(endIndex-1);
        LocalDateTime nextCursor;
        Long nextIdAfter;

        switch (sortField){
            case "startedAt" -> nextCursor = lastBackupDto==null? null:lastBackupDto.startedAt();
            case "endedAt" -> nextCursor=lastBackupDto==null? null:lastBackupDto.endedAt();
            default -> nextCursor=null;
        }
        nextIdAfter = lastBackupDto == null?null:lastBackupDto.id();


        return new CursorPageResponseBackupDto(
                content.toArray(),
                nextCursor,
                nextIdAfter,
                (long)size,
                totalElements,
                hasNext
        );
    }
}
