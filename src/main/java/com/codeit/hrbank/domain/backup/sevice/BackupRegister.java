package com.codeit.hrbank.domain.backup.sevice;

import com.codeit.hrbank.domain.backup.entity.Backup;
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
    private final EmployeeMapper employeeMapper;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    protected Backup afterRegister(Long backupId) throws Exception {
        fileStorage.put(DtoToByteArray());
        File file = fileRepository.save(new File("name", "csv", 100L
                )
        );
        Backup backup = backupRepository.findById(backupId).orElseThrow();
        backup.backupComplete(file);
        return backupRepository.save(backup);
    }

    private byte[] DtoToByteArray() throws Exception{

        List<EmployeeDto> employeeDtos = employeeRepository.findAll().stream().map(employeeMapper::toDto).toList();
        if(employeeDtos == null || employeeDtos.isEmpty()) return null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(employeeDtos);
        objectOutputStream.flush();

        byte[] resultBytes = outputStream.toByteArray();
        objectOutputStream.close();
        outputStream.close();

        return resultBytes;
    }
}
