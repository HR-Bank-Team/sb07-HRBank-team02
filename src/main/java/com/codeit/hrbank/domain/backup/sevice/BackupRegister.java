package com.codeit.hrbank.domain.backup.sevice;

import com.codeit.hrbank.domain.backup.dto.export.ExportEmployeeDto;
import com.codeit.hrbank.domain.backup.entity.Backup;
import com.codeit.hrbank.domain.backup.mapper.ExportEmployeeMapper;
import com.codeit.hrbank.domain.backup.repository.BackupRepository;
import com.codeit.hrbank.domain.employee.dto.EmployeeDto;
import com.codeit.hrbank.domain.employee.mapper.EmployeeMapper;
import com.codeit.hrbank.domain.employee.repository.EmployeeRepository;
import com.codeit.hrbank.domain.file.entity.File;
import com.codeit.hrbank.domain.file.repository.FileRepository;
import com.codeit.hrbank.domain.file.service.FileStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BackupRegister {

    private final BackupRepository backupRepository;
    private final FileStorage fileStorage;
    private final FileRepository fileRepository;
    private final EmployeeRepository employeeRepository;
    private final ExportEmployeeMapper exportEmployeeMapper;

    @Transactional
    protected Backup afterRegister(Backup backup) throws Exception {

        String fileName = backup.getStartedAt().toString().replace(":", "-") + ".csv";
        File file = fileRepository.save(new File(fileName, "csv", 100L
                )
        );
        List<ExportEmployeeDto> employeeDtos = employeeRepository.findAll().stream().map(exportEmployeeMapper::toDto).toList();
        fileStorage.putCsv(fileName, employeeDtos);
        backup.backupComplete(file);
        return backupRepository.save(backup);
    }

}
