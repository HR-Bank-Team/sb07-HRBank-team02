package com.codeit.hrbank.employee.integration.service;

import com.codeit.hrbank.domain.employee.dto.*;
import com.codeit.hrbank.domain.employee.entity.EmployeeStatus;
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
import java.util.List;
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
        void createEmployeeIllegalArgumentException() throws IOException {
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
        void getEmployeeThrowNoSuchElementException() {
            assertThrows(NoSuchElementException.class, () -> employeeService.getEmployee(9999L));
        }
    }

    @DisplayName("직원 목록 조회(커서 페이지네이션) 테스트")
    @Nested
    class GetAllEmployee {

        @Test
        @DisplayName("직원 목록 조회 - 첫 페이지 조회")
        void getAllEmployeeFirst() {

            // given
            int size = 30;

            CursorPageRequestEmployeeDto request = new CursorPageRequestEmployeeDto(
                    null, null, null, null, null, null, null, null, null,
                    size, SortField.name, SortDirection.asc
            );


            // when
            CursorPageResponseEmployeeDto allEmployee = employeeService.getAllEmployee(request);


            // 실제 페이지네이션의 마지막 직원의 ID 값 저장
            Long lastEmployeeId = null;

            if(allEmployee.content() instanceof List<?>) {
                List<?> list = (List<?>) allEmployee.content();

                if(!list.isEmpty() && list.get(0) instanceof EmployeeDto) {
                    EmployeeDto employeeDtos = (EmployeeDto) list.get(list.size() - 1);
                    lastEmployeeId = employeeDtos.id();
                }
            }

            // then
            assertEquals(size, allEmployee.size());
            assertEquals(lastEmployeeId, allEmployee.nextIdAfter()); // IdAfter 값이 잘 저장되었는지 비교
        }

        @Test
        @DisplayName("직원 목록 조회 - 이후 페이지 조회")
        void getAllEmployeeAfter() {
            // given
            int size = 30;

            CursorPageRequestEmployeeDto request = new CursorPageRequestEmployeeDto(
                    null, null, null, null, null, null, null, null, null,
                    size, SortField.name, SortDirection.asc
            );

            CursorPageResponseEmployeeDto firstPage = employeeService.getAllEmployee(request);

            // 다음 페이지
            // 다음 페이지가 존재한다는 가정(hasNext() == true)으로 테스트
            CursorPageRequestEmployeeDto request2 = new CursorPageRequestEmployeeDto(
                    null, null, null, null, null, null, null,
                    firstPage.nextIdAfter(),
                    firstPage.nextCursor(),
                    size, SortField.name, SortDirection.asc
            );

            CursorPageResponseEmployeeDto nextPage = employeeService.getAllEmployee(request2);

            // when
            assertEquals(size, nextPage.size());
        }
    }

    @DisplayName("직원 정보 수정 테스트")
    @Nested
    class UpdateEmployee {

        @Test
        @DisplayName("직원 정보 수정")
        void updateEmployee() throws IOException {
            // given
            EmployeeUpdateRequest request = new EmployeeUpdateRequest(
                    "오대식",
                    "daesik@naver.com",
                    7L,
                    "사장",
                    LocalDate.now(),
                    EmployeeStatus.ON_LEAVE,
                    "직원 정보 수정"
            );

            MockMultipartFile file = new MockMultipartFile(
                    "file",
                    "profile.png",
                    "image/png",
                    "dummy image data".getBytes()
            );

            // when
            EmployeeDto employeeDto = employeeService.updateEmployee(401L, request, file, "127.0.0.1");

            // then
            assertEquals(request.name(), employeeDto.name());
            assertEquals(request.email(), employeeDto.email());
            assertEquals(request.departmentId(), employeeDto.departmentId());
            assertEquals(request.position(), employeeDto.position());
            assertEquals(request.hireDate(), employeeDto.hireDate());
            assertEquals(EmployeeStatus.ON_LEAVE, employeeDto.status());
        }
    }

    @DisplayName("직원 삭제 테스트")
    @Nested
    class DeleteEmployee {

        @Test
        @DisplayName("직원 삭제 - 성공")
        void deleteEmployee() throws IOException {
            // given
            Long employeeId = 401L;

            // when
            employeeService.deleteEmployee(employeeId, "127.0.0.1");

            // then
            assertThrows(NoSuchElementException.class, () -> employeeService.getEmployee(employeeId));
        }

        @Test
        @DisplayName("직원 삭제 - 실패 (DB에 없는 employeeId로 삭제)")
        void deleteEmployeeNoSuchElementException() throws IOException {
            // given
            Long employeeId = 9999L;

            // when & then
            assertThrows(NoSuchElementException.class, () -> employeeService.deleteEmployee(employeeId, "127.0.0.1"));
        }
    }

    @DisplayName("직원 수 조회 테스트")
    @Nested
    class GetEmployeeCount {

        @Test
        @DisplayName("직원 수 조회")
        void getEmployeeCount() {
            // given


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

        @Test
        @DisplayName("직원 수 추이 조회")
        void getEmployeeTrend() {
            // given
            EmployeeTrendRequest request = new EmployeeTrendRequest(
                    LocalDate.now().minusDays(12),
                    LocalDate.now(),
                    TimeUnit.day
            );

            // when
            List<EmployeeTrendDto> employeeTrend = employeeService.getEmployeeTrend(request);

            // then
            assertEquals(13, employeeTrend.size());
        }
    }
}