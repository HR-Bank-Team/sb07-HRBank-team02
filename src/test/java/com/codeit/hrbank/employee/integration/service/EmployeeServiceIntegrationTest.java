package com.codeit.hrbank.employee.integration.service;

import com.codeit.hrbank.domain.employee.dto.EmployeeCreateRequest;
import com.codeit.hrbank.domain.employee.dto.EmployeeDistributionDto; //추가
import com.codeit.hrbank.domain.employee.dto.EmployeeDto;
import com.codeit.hrbank.domain.employee.entity.EmployeeStatus; //추가
import com.codeit.hrbank.domain.employee.service.EmployeeService;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext; //추가
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;  //추가
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS) //추가
class EmployeeServiceIntegrationTest {
    @Autowired
    EntityManager em;

    @BeforeEach
    void cleanDB() {
        em.createNativeQuery("TRUNCATE TABLE change_logs CASCADE").executeUpdate();
        em.createNativeQuery("TRUNCATE TABLE diffs CASCADE").executeUpdate();
        em.createNativeQuery("TRUNCATE TABLE employees CASCADE").executeUpdate();
        em.createNativeQuery("TRUNCATE TABLE files CASCADE").executeUpdate();
        em.createNativeQuery("TRUNCATE TABLE backups CASCADE").executeUpdate();
    }

    @Autowired
    private EmployeeService employeeService;

    private MockMultipartFile mockFile() {
        return new MockMultipartFile(
                "file",
                "profile.png",
                "image/png",
                "dummy image".getBytes()
        );
    }

    // ============================================================
    @DisplayName("직원 등록 테스트")
    @Nested
    class CreateEmployee {

        @Test
        @DisplayName("직원 등록 - 성공")
        void createEmployee() throws IOException {

            EmployeeCreateRequest request = new EmployeeCreateRequest(
                    "김춘식",
                    "chunsik@kakao.com",
                    5L,
                    "신입",
                    LocalDate.now(),
                    "직원 생성"
            );

            EmployeeDto employee = employeeService.createEmployee(request, mockFile(), "127.0.0.1");

            assertEquals(request.name(), employee.name());
            assertEquals(request.email(), employee.email());
            assertEquals(request.departmentId(), employee.departmentId());
            assertEquals(request.position(), employee.position());
            assertEquals(request.hireDate(), employee.hireDate());
        }

        @Test
        @DisplayName("직원 등록 - 실패 (이메일 중복)")
        void createEmployee_whenDuplicateEmail_thenThrow() throws IOException {

            EmployeeCreateRequest req1 = new EmployeeCreateRequest(
                    "김춘식",
                    "dup@kakao.com",
                    5L,
                    "신입",
                    LocalDate.now(),
                    "생성"
            );

            EmployeeCreateRequest req2 = new EmployeeCreateRequest(
                    "이춘식",
                    "dup@kakao.com",
                    7L,
                    "인턴",
                    LocalDate.now(),
                    "생성"
            );

            employeeService.createEmployee(req1, mockFile(), "127.0.0.1");

            assertThrows(IllegalArgumentException.class,
                    () -> employeeService.createEmployee(req2, mockFile(), "127.0.0.1"));
        }
    }

    // ============================================================
    @DisplayName("직원 상세 조회 테스트")
    @Nested
    class GetEmployee {

        @Test
        @DisplayName("직원 단일 조회 - 성공")
        void getEmployee() throws IOException {

            EmployeeCreateRequest request = new EmployeeCreateRequest(
                    "김춘식",
                    "findme@kakao.com",
                    5L,
                    "신입",
                    LocalDate.now(),
                    "생성"
            );

            Long id = employeeService.createEmployee(request, mockFile(), "127.0.0.1").id();

            EmployeeDto employee = employeeService.getEmployee(id);

            assertEquals(request.name(), employee.name());
            assertEquals(request.email(), employee.email());
        }

        @Test
        @DisplayName("직원 단일 조회 - 실패 (존재하지 않는 ID)")
        void getEmployee_notFound() {
            assertThrows(NoSuchElementException.class,
                    () -> employeeService.getEmployee(9999L));
        }
    }

    // ============================================================
    @DisplayName("직원 수 조회 테스트")
    @Nested
    class GetEmployeeCount {

        @Test
        @DisplayName("직원 수 조회 - 성공")
        void getEmployeeCount() throws IOException {

            EmployeeCreateRequest req1 = new EmployeeCreateRequest(
                    "직원1", "a@a.com", 1L, "신입", LocalDate.now(), "메모"
            );

            EmployeeCreateRequest req2 = new EmployeeCreateRequest(
                    "직원2", "b@a.com", 1L, "신입", LocalDate.now(), "메모"
            );

            employeeService.createEmployee(req1, mockFile(), "127.0.0.1");
            employeeService.createEmployee(req2, mockFile(), "127.0.0.1");

            long count = employeeService.getEmployeeCount(
                    null,
                    LocalDate.now().minusDays(1),
                    LocalDate.now().plusDays(1)
            );

            assertEquals(2L, count);
        }
    }

    // ============================================================
    @DisplayName("직원 분포 조회 테스트")
    @Nested
    class GetEmployeeDistribution {

        @Test
        @DisplayName("직원 분포 조회 - 부서별 분포의 합이 전체 직원 수와 같다")
        void getEmployeeDistribution_department() throws IOException {
            // given - 직원 몇 명은 테스트에서 직접 추가해 놓자 (있어도 되고, 이미 DB에 있어도 상관 없음)
            employeeService.createEmployee(
                    new EmployeeCreateRequest("테스트직원1", "dist1@test.com", 1L, "신입", LocalDate.now(), "테스트"),
                    mockFile(),
                    "127.0.0.1"
            );

            employeeService.createEmployee(
                    new EmployeeCreateRequest("테스트직원2", "dist2@test.com", 2L, "신입", LocalDate.now(), "테스트"),
                    mockFile(),
                    "127.0.0.1"
            );

            // when - 부서별 직원 분포 조회
            List<EmployeeDistributionDto> result =
                    employeeService.getEmployeeDistribution("department", null);

            // 전체 직원 수
            long totalCount = employeeService.getEmployeeCount(null, null, null);

            // 분포에서 나온 count 합
            long sumOfDistribution = result.stream()
                    .mapToLong(EmployeeDistributionDto::count)
                    .sum();

            // then
            // 1) 분포 결과가 비어있진 않아야 하고
            assertFalse(result.isEmpty());

            // 2) 분포의 count 합 = 전체 직원 수 여야 한다
            assertEquals(totalCount, sumOfDistribution);
        }
    }

}
