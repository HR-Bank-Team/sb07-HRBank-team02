package com.codeit.hrbank.domain.employee.controller;

import com.codeit.hrbank.domain.employee.dto.EmployeeCreateRequest;
import com.codeit.hrbank.domain.employee.dto.EmployeeDto;
import com.codeit.hrbank.domain.employee.dto.EmployeeUpdateRequest;
import com.codeit.hrbank.domain.employee.sevice.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
        return ResponseEntity.ok(responses); // 200 OK
    }

    // 직원 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getEmployee(@PathVariable Long id) {
        EmployeeDto response = employeeService.getEmployee(id);
        return ResponseEntity.ok(response); // 200 OK
    }

    // 직원 등록
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EmployeeDto> createEmployee(
            @RequestPart("employee") EmployeeCreateRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        EmployeeDto response = employeeService.createEmployee(request, file);
        return ResponseEntity
                .status(HttpStatus.CREATED) // 201 Created
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
        return ResponseEntity.ok(response); // 200 OK
    }

    // 직원 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
