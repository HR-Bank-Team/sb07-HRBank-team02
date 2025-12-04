package com.codeit.hrbank.domain.backup.entity;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class BackupSortDirectionConverter implements Converter<String, BackupSortDirection> {

    @Override
    public BackupSortDirection convert(String source) {
        if(source == null) return null;
        return BackupSortDirection.valueOf(source.
                replaceAll("([a-z])([A-Z])", "$1_$2").
                toUpperCase());
    }


}
