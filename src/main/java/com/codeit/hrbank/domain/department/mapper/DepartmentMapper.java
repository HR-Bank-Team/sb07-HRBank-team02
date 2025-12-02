package com.codeit.hrbank.domain.department.mapper;

import com.codeit.hrbank.domain.department.dto.DepartmentCreateRequest;
import com.codeit.hrbank.domain.department.dto.DepartmentDto;
import com.codeit.hrbank.domain.department.entity.Department;
import com.codeit.hrbank.domain.employee.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DepartmentMapper {

    private final EmployeeRepository employeeRepository;

    public DepartmentDto toDto(Department department) {
        Long totalEmployeeNumber = employeeRepository.countByDepartmentId(department.getId());
        return new DepartmentDto(department.getId(),
                department.getName(),
                department.getDescription(),
                department.getEstablishedDate(),
                totalEmployeeNumber);
    }

    public Department toEntity(DepartmentCreateRequest departmentCreateRequest) {
        return new Department(
                departmentCreateRequest.name(),
                departmentCreateRequest.description(),
                departmentCreateRequest.establishedDate()
        );
    }
}
