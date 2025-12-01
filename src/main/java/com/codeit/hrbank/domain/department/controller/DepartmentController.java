package com.codeit.hrbank.domain.department.controller;

import com.codeit.hrbank.domain.department.dto.CursorPageResponseDepartmentDto;
import com.codeit.hrbank.domain.department.dto.DepartmentDto;
import com.codeit.hrbank.domain.department.dto.DepartmentUpdateRequest;
import com.codeit.hrbank.domain.department.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentRepository departmentRepository;

    @GetMapping
    public ResponseEntity<CursorPageResponseDepartmentDto> getAllDepartments(){
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentDto> getDepartment(@PathVariable Long id){
        return null;
    }

    @PostMapping
    public ResponseEntity<DepartmentDto> createDepartment(){
        return null;
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteDepartment(){
        return null;

    }

    @PatchMapping
    public ResponseEntity<DepartmentUpdateRequest> updateDepartment(){
        return null;
    }


}
