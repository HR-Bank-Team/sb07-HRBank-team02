package com.codeit.hrbank.domain.employee.controller;

import com.codeit.hrbank.domain.employee.dto.EmployeeCreateRequest;
import com.codeit.hrbank.domain.employee.dto.EmployeeDto;
import com.codeit.hrbank.domain.employee.dto.EmployeeUpdateRequest;
import com.codeit.hrbank.domain.employee.dto.EmployeeCountDto;
import com.codeit.hrbank.domain.employee.dto.EmployeeDistributionDto;
import com.codeit.hrbank.domain.employee.entity.EmployeeStatus;
import com.codeit.hrbank.domain.employee.sevice.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    // 직원 전체 조회
    @GetMapping
    public ResponseEntity<List<EmployeeDto>> getAllEmployee() {
        List<EmployeeDto> responses = employeeService.getAllEmployee();
        return ResponseEntity.ok(responses);
    }

    // 직원 단건 조회
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
    //직원 수 조회
    @GetMapping("/count")
    public ResponseEntity<EmployeeCountDto> getEmployeeCount(
            @RequestParam(required = false) EmployeeStatus status,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            OffsetDateTime fromDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            OffsetDateTime toDate
    ) {

        LocalDateTime from = (fromDate != null) ? fromDate.toLocalDateTime() : null;
        LocalDateTime to = (toDate != null) ? toDate.toLocalDateTime() : null;

        if (from != null && to == null) {
            to = LocalDateTime.now();
        }

        EmployeeCountDto response = employeeService.getEmployeeCount(status, from, to);
        return ResponseEntity.ok(response);
    }

    // 직원 분포 조회
    @GetMapping("/stats/distribution")
    public ResponseEntity<List<EmployeeDistributionDto>> getEmployeeDistribution(
            @RequestParam(defaultValue = "department") String groupBy,  // department / position
            @RequestParam(defaultValue = "ACTIVE") EmployeeStatus status
    ) {
        List<EmployeeDistributionDto> response =
                employeeService.getEmployeeDistribution(groupBy, status);

        return ResponseEntity.ok(response);
    }
}

