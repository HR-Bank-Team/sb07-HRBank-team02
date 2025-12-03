package com.codeit.hrbank.file.integration.service;

import com.codeit.hrbank.backup.util.TestFixture;
import com.codeit.hrbank.domain.backup.dto.response.BackupDto;
import com.codeit.hrbank.domain.backup.sevice.BackupRegister;
import com.codeit.hrbank.domain.backup.sevice.BackupService;
import com.codeit.hrbank.domain.changelog.repository.ChangeLogRepository;
import com.codeit.hrbank.domain.department.entity.Department;
import com.codeit.hrbank.domain.department.repository.DepartmentRepository;
import com.codeit.hrbank.domain.employee.repository.EmployeeRepository;
import com.codeit.hrbank.domain.file.repository.FileRepository;
import com.codeit.hrbank.domain.file.service.FileService;
import com.codeit.hrbank.domain.file.service.FileStorage;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class FileStorageTest {

    @Autowired
    FileStorage fileStorage;

    @Autowired
    FileService fileService;

    @Autowired
    BackupRegister  backupRegister;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    TestFixture fixture;

    @Autowired
    FileRepository fileRepository;

    @Autowired
    ChangeLogRepository  changeLogRepository;

    @Test
    @DisplayName("[정상 케이스] csv 파일 저장")
    void saveCsv() throws Exception {
        //given
        changeLogRepository.save(fixture.changeLogFactory());
        BackupDto backup = backupRegister.createBackup("1.5.5.7");
        Long fileId = backup.fileId();

        //when
        var actualResult= fileRepository.findById(fileId).isPresent();

        //then
        assertTrue(actualResult);
        assertDoesNotThrow(()->fileStorage.get(fileId));
    }

    @Test
    @DisplayName("[정상 케이스] 프로필 파일 저장")
    void saveProfile(){

        //given

        //when

        //then
    }

    @Test
    @DisplayName("[정상 케이스] csv 파일 다운로드")
    void downloadCsv() throws Exception {

        //given
        changeLogRepository.save(fixture.changeLogFactory());
        BackupDto backup = backupRegister.createBackup("1.5.5.7");
        Long fileId = backup.fileId();

        //when
        ResponseEntity<Resource> resourceResponseEntity = fileService.downloadFileService(fileId);

        //then
        var actualResult = resourceResponseEntity.getBody().getContentAsByteArray();
        var expectedResult = fileStorage.get(fileId).readAllBytes();

        assertArrayEquals(actualResult, expectedResult );
    }

    @Test
    @DisplayName("[정상 케이스] 프로필 파일 다운로드")
    void downloadProfile(){

        //given

        //when

        //then
    }
}
