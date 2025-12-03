package com.codeit.hrbank.domain.employee.controller.docs;

import com.codeit.hrbank.domain.employee.dto.*;
import com.codeit.hrbank.domain.employee.entity.Employee;
import com.codeit.hrbank.domain.employee.entity.EmployeeStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Tag(name = "직원 관리", description = "직원 관리 API")
public interface EmployeeControllerDocs {
//직원 목록 조회
    @Operation(
            summary = "직원 목록 조회",
            description = "직원 목록을 조회합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                            mediaType = "*/*",
                            schema = @Schema(implementation = Employee.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "id": 0,
                                        "name": "홍길동",
                                        "email": "hong@example.com",
                                        "employeeNumber": "EMP-2025-9f0a098c-9443-49ed-95cb-9ae461259b0b",
                                        "departmentId": 1,
                                        "departmentName": "재무부",
                                        "position": "대리",
                                        "hireDate": "2022-12-04",
                                        "status": "ACTIVE",
                                        "profileImageId": 1
                                      }
                                    """)
                    )
            )
    })

    ResponseEntity<CursorPageResponseEmployeeDto> getAllEmployee(CursorPageRequestEmployeeDto request);

//직원 등록
    @Operation(
            summary = "직원 등록",
            description = "새로운 직원을 등록합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "등록 성공",
                    content = @Content(
                            mediaType = "*/*",
                            schema = @Schema(implementation = EmployeeDto.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "id": 1,
                                      "name": "홍길동",
                                      "email": "hong@example.com",
                                      "employeeNumber": "EMP-2023-001",
                                      "departmentId": 1,
                                      "departmentName": "개발팀",
                                      "position": "선임 개발자",
                                      "hireDate": "2023-01-01",
                                      "status": "재직중",
                                      "profileImageId": 1
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 또는 중복된 이메일",
                    content = @Content(
                            mediaType = "*/*",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "timestamp": "2025-03-06T05:39:06.152068Z",
                                      "status": 400,
                                      "message": "잘못된 요청입니다.",
                                      "details": "부서 코드는 필수입니다."
                                    }
                                    """)

                    )

            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "잘못된 요청 또는 중복된 이메일",
                    content = @Content(
                            mediaType = "*/*",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "timestamp": "2025-03-06T05:39:06.152068Z",
                                      "status": 400,
                                      "message": "잘못된 요청입니다.",
                                      "details": "부서 코드는 필수입니다."
                                    }
                                    """)

                    )

            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "잘못된 요청 또는 중복된 이메일",
                    content = @Content(
                            mediaType = "*/*",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "timestamp": "2025-03-06T05:39:06.152068Z",
                                      "status": 400,
                                      "message": "잘못된 요청입니다.",
                                      "details": "부서 코드는 필수입니다."
                                    }
                                    """)

                    )

            )

    })
    ResponseEntity<EmployeeDto> createEmployee(EmployeeCreateRequest request, MultipartFile file, HttpServletRequest servletRequest) throws IOException;
// 직원 상세 조회
    @Operation(
            summary = "직원 상세 조회",
            description = "직원 상세 정보를 조회합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                            mediaType = "*/*",
                            schema = @Schema(implementation = Employee.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "id": 5,
                                      "name": "직원17",
                                      "email": "employee17@example.com",
                                      "employeeNumber": "EMP-2025-9f0a098c-9443-49ed-95cb-9ae461259b0b",
                                      "departmentId": 4,
                                      "departmentName": "재무부",
                                      "position": "대리",
                                      "hireDate": "2022-12-04",
                                      "status": "ACTIVE",
                                      "profileImageId": 5
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "조회 실패",
                    content = @Content(
                            mediaType = "*/*",
                            schema = @Schema(implementation = Employee.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "timestamp": "2025-12-03T02:20:11.984Z",
                                      "status": 500,
                                      "error": "Internal Server Error",
                                      "path": "/api/employees/410"
                                    }
                                    """)
                    )
            )
    })

    ResponseEntity<EmployeeDto> getEmployee(Long id);

//직원 삭제
    @Operation(summary = "직원 삭제", description = "직원을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "삭제 성공"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류",
                    content = @Content(
                            mediaType = "*/*",
                            schema = @Schema(implementation = Employee.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "timestamp": "2025-12-03T02:24:19.575Z",
                                      "status": 500,
                                      "error": "Internal Server Error",
                                      "path": "/api/employees/5"
                                    }
                                    """)
                    )
            )


    })


    ResponseEntity<Void> deleteEmployee(Long id, HttpServletRequest servletRequest) throws IOException;
//직원 수정
    @Operation(summary = "직원 수정", description = "직원의 정보를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "수정 성공",
                    content = @Content(
                            mediaType = "*/*",
                            schema = @Schema(implementation = Employee.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "id": 7,
                                        "name": "홍길동",
                                        "email": "hong@test.com",
                                        "employeeNumber": "EMP-2025-eb000cf9-6dcd-4143-822f-f0c0d6a7fa3b",
                                        "departmentId": 1,
                                        "departmentName": "영업부",
                                        "position": "개발자",
                                        "hireDate": "2024-01-01",
                                        "status": "ACTIVE",
                                        "profileImageId": 7
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 또는 중복된 이메일",
                    content = @Content(
                            mediaType = "*/*",
                            schema = @Schema(implementation = Employee.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "timestamp": "2025-03-06T05:39:06.152068Z",
                                      "status": 400,
                                      "message": "잘못된 요청입니다.",
                                      "details": "부서 코드는 필수입니다."
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "직원 또는 부서를 찾을 수 없음",
                    content = @Content(
                            mediaType = "*/*",
                            schema = @Schema(implementation = Employee.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "timestamp": "2025-03-06T05:39:06.152068Z",
                                      "status": 400,
                                      "message": "잘못된 요청입니다.",
                                      "details": "부서 코드는 필수입니다."
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류",
                    content = @Content(
                            mediaType = "*/*",
                            schema = @Schema(implementation = Employee.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "timestamp": "2025-03-06T05:39:06.152068Z",
                                      "status": 400,
                                      "message": "잘못된 요청입니다.",
                                      "details": "부서 코드는 필수입니다."
                                    }
                                    """)
                    )
            )
    })

    ResponseEntity<EmployeeDto> updateEmployee(Long id, EmployeeUpdateRequest request, MultipartFile file, HttpServletRequest servletRequest) throws IOException;
//직원 증감 추이
    @Operation(summary = "직원 증감 추이", description = "직원의 시간별 증감 추이를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                            mediaType = "*/*",
                            schema = @Schema(implementation = Employee.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "date": "2013-12-03",
                                        "count": 0,
                                        "change": 0,
                                        "changeRate": 0.0
                                        }
                                    """)
                    )
            )
    })

    ResponseEntity<List<EmployeeTrendDto>> trendEmployee(EmployeeTrendRequest employeeTrendRequest);
//직원 분포 조회
    @Operation(summary = "직원 분포 조회", description = "지정된 기준으로 그룹화된 직원 분포를 조회합니다.")

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                            mediaType = "*/*",
                            schema = @Schema(implementation = Employee.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "groupKey": "마케팅부",
                                        "count": 83,
                                        "percentage": 20.75
                                    }
                                    """)
                    )
            )
    })

    ResponseEntity<List<EmployeeDistributionDto>> getEmployeeDistribution(String groupBy, EmployeeStatus status);
//직원 수 조회
    @Operation(summary = "직원 수 조회", description = "지정된 조건에 맞는 직원 수를 조회합니다. 상태 필터링 및 입사일 기간 필터링이 가능합니다.")

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                            mediaType = "*/*",
                            schema = @Schema(implementation = Employee.class),
                            examples = @ExampleObject(value = """
                                    397
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "조회 실패 올바른 날짜를 입력하세요",
                    content = @Content(
                            mediaType = "*/*",
                            schema = @Schema(implementation = Employee.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "timestamp": "2025-12-03T02:36:24.341Z",
                                      "status": 400,
                                      "error": "Bad Request",
                                      "path": "/api/employees/count"
                                    }
                                    """)
                    )
            )
    })

    ResponseEntity<Long> getEmployeeCount(
            EmployeeStatus status,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate
    );
}