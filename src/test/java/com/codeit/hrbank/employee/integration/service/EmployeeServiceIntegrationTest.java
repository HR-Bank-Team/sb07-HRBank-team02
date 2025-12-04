package com.codeit.hrbank.employee.integration.service;

import com.codeit.hrbank.domain.employee.dto.EmployeeCreateRequest;
import com.codeit.hrbank.domain.employee.dto.EmployeeDto;
import com.codeit.hrbank.domain.employee.service.EmployeeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class EmployeeServiceIntegrationTest {

    @Autowired
    private EmployeeService employeeService;

    @DisplayName("직원 등록 테스트")
    @Nested
    class CreateEmployee {
        // 성공 케이스
        @Test
        @DisplayName("직원 등록 - 성공")
        void createEmployee() throws IOException {
            // given
            EmployeeCreateRequest request =
                    new EmployeeCreateRequest(
                            "김춘식",
                            "chunsik@kakao.com",
                            5L,
                            "신입",
                            LocalDate.now(),
                            "직원 생성"
                    );

            MockMultipartFile file = new MockMultipartFile(
                    "file",
                    "profile.png",
                    "image/png",
                    "dummy image data".getBytes()
            );

            // when
            EmployeeDto employee = employeeService.createEmployee(request, file, "127.0.0.1");

            // then
            assertEquals(request.name(), employee.name());
            assertEquals(request.email(), employee.email());
            assertEquals(request.departmentId(), employee.departmentId());
            assertEquals(request.position(), employee.position());
            assertEquals(request.hireDate(), employee.hireDate());
        }

        // 실패 케이스
        @Test
        @DisplayName("직원 등록 - 실패 (이메일 중복)")
        void createEmployee_when_duplicate_email_then_illegalArgumentException() throws IOException {
            // given
            EmployeeCreateRequest request =
                    new EmployeeCreateRequest(
                            "김춘식",
                            "chunsik@kakao.com",
                            5L,
                            "신입",
                            LocalDate.now(),
                            "직원 생성"
                    );

            EmployeeCreateRequest request2 =
                    new EmployeeCreateRequest(
                            "이춘식",
                            "chunsik@kakao.com",
                            6L,
                            "신입",
                            LocalDate.now(),
                            "직원 생성"
                    );

            MockMultipartFile file = new MockMultipartFile(
                    "file",
                    "profile.png",
                    "image/png",
                    "dummy image data".getBytes()
            );

            // when & then
            employeeService.createEmployee(request, file, "127.0.0.1");
            assertThrows(IllegalArgumentException.class, () -> employeeService.createEmployee(request2, file, "127.0.0.1"));
        }
    }

    @DisplayName("직원 상세 조회 테스트")
    @Nested
    class GetEmployee {
        @Test
        @DisplayName("직원 단일 조회 - 성공 ")
        void getEmployee() throws IOException {

            // given
            EmployeeCreateRequest request =
                    new EmployeeCreateRequest(
                            "김춘식",
                            "chunsik@kakao.com",
                            5L,
                            "신입",
                            LocalDate.now(),
                            "직원 생성"
                    );

            MockMultipartFile file = new MockMultipartFile(
                    "file",
                    "profile.png",
                    "image/png",
                    "dummy image data".getBytes()
            );

            Long id = employeeService.createEmployee(request, file, "127.0.0.1").id();

            // when
            EmployeeDto employee = employeeService.getEmployee(id);

            // then
            assertEquals(request.name(), employee.name());
            assertEquals(request.email(), employee.email());
            assertEquals(request.departmentId(), employee.departmentId());
            assertEquals(request.position(), employee.position());
            assertEquals(request.hireDate(), employee.hireDate());
        }

        @Test
        @DisplayName("직원 단일 조회 - 실패 (DB에 없는 employeeId로 조회)")
        void getEmployee_when_not_found_then_throw_NoSuchElementException() {
            assertThrows(NoSuchElementException.class, () -> employeeService.getEmployee(9999L));
        }
    }

    @DisplayName("직원 목록 조회 테스트")
    @Nested
    class GetAllEmployee {
    }


    @DisplayName("직원 정보 수정 테스트")
    @Nested
    class UpdateEmployee {
    }

    @DisplayName("직원 삭제 테스트")
    @Nested
    class DeleteEmployee {
    }

    @DisplayName("직원 수 조회 테스트")
    @Nested
    class GetEmployeeCount {

        @Test
        @DisplayName("직원 수 조회")
        void getEmployeeCount() {
            // given
            EmployeeCreateRequest request1 = new EmployeeCreateRequest(
                    "홍길동",
                    "rlfehd3333@test.com",
                    1L,
                    "신입 개발자",
                    LocalDate.now(),
                    "신입"
            );

            // when


            // then
        }
    }

    @DisplayName("직원 분포 조회 테스트")
    @Nested
    class GetEmployeeDistribution {

        @Test
        @DisplayName("직원 분포 조회")
        void getEmployeeDistribution() {
            // given


            // when


            // then
        }
    }

    @DisplayName("직원 수 추이 조회 테스트")
    @Nested
    class GetEmployeeTrend {
    }


}