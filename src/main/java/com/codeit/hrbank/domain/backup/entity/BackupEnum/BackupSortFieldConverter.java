package com.codeit.hrbank.domain.backup.entity.BackupEnum;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class BackupSortFieldConverter implements Converter<String, BackupSortField> {

    @Override
    public BackupSortField convert(String source) {
        if(source == null) return null;
        return BackupSortField.valueOf(
                source.replaceAll("([a-z])([A-Z])", "$1_$2")
                        .toUpperCase()
        );
    }
}
