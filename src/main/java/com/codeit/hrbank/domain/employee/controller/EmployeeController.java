package com.codeit.hrbank.domain.employee.controller;

import com.codeit.hrbank.domain.employee.controller.docs.EmployeeControllerDocs;
import com.codeit.hrbank.domain.employee.dto.*;
import com.codeit.hrbank.domain.employee.entity.EmployeeStatus;
import com.codeit.hrbank.domain.employee.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/employees")
public class EmployeeController implements EmployeeControllerDocs {

    private final EmployeeService employeeService;

    // 직원 목록 조회
    @GetMapping
    public ResponseEntity<List<EmployeeDto>> getAllEmployee() {
        List<EmployeeDto> responses = employeeService.getAllEmployee();
        return ResponseEntity.ok(responses);
    }

    // 직원 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getEmployee(@PathVariable Long id) {
        EmployeeDto response = employeeService.getEmployee(id);
        return ResponseEntity.ok(response);
    }

    // 직원 등록
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EmployeeDto> createEmployee(
            @RequestPart("employee") EmployeeCreateRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        EmployeeDto response = employeeService.createEmployee(request, file);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // 직원 정보 수정
    @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EmployeeDto> updateEmployee(
            @PathVariable Long id,
            @RequestPart("employee") EmployeeUpdateRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        EmployeeDto response = employeeService.updateEmployee(id, request, file);
        return ResponseEntity.ok(response);
    }

    // 직원 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

    // 직원 수 조회 (LocalDate 기반)
    @GetMapping("/count")
    public ResponseEntity<Long> getEmployeeCount(
            @RequestParam(required = false) EmployeeStatus status,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fromDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate toDate
    ) {

        long count = employeeService.getEmployeeCount(status, fromDate, toDate);
        return ResponseEntity.ok(count);
    }


    // 직원 분포 조회
    @GetMapping("/stats/distribution")
    public ResponseEntity<List<EmployeeDistributionDto>> getEmployeeDistribution(
            @RequestParam(defaultValue = "department") String groupBy,
            @RequestParam(defaultValue = "ACTIVE") EmployeeStatus status
    ) {
        List<EmployeeDistributionDto> response =
                employeeService.getEmployeeDistribution(groupBy, status);

        return ResponseEntity.ok(response);
    }

    // 직원 증감 추이 조회 (Trend)
    @GetMapping("/stats/trend")
    public ResponseEntity<List<EmployeeTrendDto>> trendEmployee(
            @ModelAttribute EmployeeTrendRequest employeeTrendRequest
    ) {
        List<EmployeeTrendDto> employeeTrend =
                employeeService.getEmployeeTrend(employeeTrendRequest);

        return ResponseEntity.ok(employeeTrend);
    }
}
