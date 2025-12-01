package com.codeit.hrbank.domain.backup.mapper;

import com.codeit.hrbank.domain.backup.dto.response.CursorPageResponseBackupDto;
import com.codeit.hrbank.domain.backup.entity.Backup;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

@Component
public class CursorPageBackupMapper {

    public CursorPageResponseBackupDto toDto(Slice<Backup> backupSlice,Object nextCursor){

        return new CursorPageResponseBackupDto(
                backupSlice.getContent().toArray(),
                (String) nextCursor,
                backupSlice.nextPageable().getOffset(),
                (long)backupSlice.getSize(),
                (long)backupSlice.getNumberOfElements(),
                backupSlice.hasNext()
        );
    }
}
