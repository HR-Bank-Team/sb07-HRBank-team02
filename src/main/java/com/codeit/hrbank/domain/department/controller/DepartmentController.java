package com.codeit.hrbank.domain.department.controller;

import com.codeit.hrbank.domain.department.dto.*;
import com.codeit.hrbank.domain.department.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @GetMapping
    public ResponseEntity<CursorPageResponseDepartmentDto> getAllDepartments(CursorPageRequestDepartmentDto request){
        return ResponseEntity.ok(departmentService.getAllDepartments(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentDto> getDepartment(@PathVariable Long id){
        return ResponseEntity.ok(departmentService.getDepartment(id));
    }

    @PostMapping
    public ResponseEntity<DepartmentDto> createDepartment(@Valid @RequestBody DepartmentCreateRequest request){
        DepartmentDto dto = departmentService.createDepartment(request);
        return ResponseEntity.created(URI.create("/api/departments/" + dto.id())).body(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id){
        departmentService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DepartmentDto> updateDepartment(
            @PathVariable Long id,
            @Valid @RequestBody DepartmentUpdateRequest request
    ){
        DepartmentDto departmentDto = departmentService.updateDepartment(id, request);
        return ResponseEntity.ok(departmentDto);
    }
}
