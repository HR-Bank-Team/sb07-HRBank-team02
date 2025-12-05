package com.codeit.hrbank.domain.department.mapper;

import com.codeit.hrbank.domain.department.dto.DepartmentCreateRequest;
import com.codeit.hrbank.domain.department.dto.DepartmentDto;
import com.codeit.hrbank.domain.department.entity.Department;
import com.codeit.hrbank.domain.department.projection.DepartmentWithCountEmployee;
import com.codeit.hrbank.domain.employee.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DepartmentMapper {

    private final EmployeeRepository employeeRepository;

    // 단건 상세용: Department 엔티티 → DTO 매핑
    // employeeCount 는 Department당 1회 countByDepartmentId 호출로 계산
    public DepartmentDto toDto(Department department) {
        Long totalEmployeeNumber = employeeRepository.countByDepartmentId(department.getId());
        return new DepartmentDto(department.getId(),
                department.getName(),
                department.getDescription(),
                department.getEstablishedDate(),
                totalEmployeeNumber);
    }

    // 목록/집계용: DepartmentWithCountEmployee 프로젝션 → DTO 매핑
    // (쿼리에서 이미 count까지 가져와서 N+1 없이 사용)
    public DepartmentDto toDto(DepartmentWithCountEmployee departmentWithCountEmployee) {
        return new DepartmentDto(departmentWithCountEmployee.getId(), // 여기부터, 테스트
                departmentWithCountEmployee.getName(),
                departmentWithCountEmployee.getDescription(),
                departmentWithCountEmployee.getEstablishedDate(),
                departmentWithCountEmployee.getEmployeeCount());
    }

    public Department toEntity(DepartmentCreateRequest departmentCreateRequest) {
        return new Department(
                departmentCreateRequest.name(),
                departmentCreateRequest.description(),
                departmentCreateRequest.establishedDate()
        );
    }
}
