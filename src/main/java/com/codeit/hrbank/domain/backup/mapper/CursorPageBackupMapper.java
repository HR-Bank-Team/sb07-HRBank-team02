package com.codeit.hrbank.domain.backup.mapper;

import com.codeit.hrbank.domain.backup.dto.response.CursorPageResponseBackupDto;
import com.codeit.hrbank.domain.backup.entity.Backup;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CursorPageBackupMapper {

    public CursorPageResponseBackupDto toDto(Slice<Backup> backupSlice, LocalDateTime nextCursor){

        return new CursorPageResponseBackupDto(
                backupSlice.getContent().toArray(),
                 nextCursor,
                backupSlice.nextPageable().getOffset(),
                (long)backupSlice.getSize(),
                (long)backupSlice.getNumberOfElements(),
                backupSlice.hasNext()
        );
    }
}
