package com.codeit.hrbank.backup.integration.service;

import com.codeit.hrbank.domain.backup.dto.response.BackupDto;
import com.codeit.hrbank.domain.backup.repository.BackupRepository;
import com.codeit.hrbank.domain.backup.sevice.BackupService;
import com.codeit.hrbank.domain.changelog.repository.ChangeLogRepository;
import com.codeit.hrbank.domain.department.entity.Department;
import com.codeit.hrbank.domain.department.repository.DepartmentRepository;
import com.codeit.hrbank.domain.employee.repository.EmployeeRepository;
import com.codeit.hrbank.backup.util.TestFixture;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class backupServiceTest {

    private static final Logger log = LoggerFactory.getLogger(backupServiceTest.class);


    @Autowired
    private BackupService backupService;

    @Autowired
    private ChangeLogRepository changeLogRepository;

    @Autowired
    TestFixture fixture;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Test
    @DisplayName("[정상 케이스] 백업 생성")
    void backupCreate() throws Exception {
        Department department = departmentRepository.save(fixture.departmentFactory());
        employeeRepository.save(fixture.employeeFactory(department));
        changeLogRepository.save(fixture.changeLogFactory());
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        BackupDto backup = backupService.createBackup(request);

        assertNotNull(backup);
    }

    @Test
    @DisplayName("[정상 케이스] 최근 백업 조회")
    void getLatestBackup() throws Exception {
        //given
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        BackupDto LatestBackup = backupService.createBackup(request);

        //when

        BackupDto backup = backupService.getLatestBackup();

        //then
        assertEquals(LatestBackup.id(),backup.id());
        
    }
}
