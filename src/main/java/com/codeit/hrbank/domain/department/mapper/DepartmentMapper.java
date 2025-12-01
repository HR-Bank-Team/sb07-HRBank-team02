package com.codeit.hrbank.domain.department.mapper;

import com.codeit.hrbank.domain.department.dto.DepartmentCreateRequest;
import com.codeit.hrbank.domain.department.dto.DepartmentDto;
import com.codeit.hrbank.domain.department.entity.Department;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DepartmentMapper {

//    private final EmployeeRepository employeeRepository; // TODO: 직원 EmployeeRepository 생성된이후 이부분으로 리펙토링하기

    public DepartmentDto toDto(Department department) {
//        Long totalEmployeeNumber = employeeRepository.countByDepartmentId(); // TODO: 직원 EmployeeRepository 생성된이후 이부분으로 리펙토링하기
        Long totalEmployeeNumber = 999L;
        return new DepartmentDto(department.getId(),
                department.getName(),
                department.getDescription(),
                department.getEstablishedDate().toLocalDate(),
                totalEmployeeNumber);
    }

    public Department toEntity(DepartmentCreateRequest departmentCreateRequest) {
            return new Department(
                    departmentCreateRequest.name(),
                    departmentCreateRequest.description(),
                    departmentCreateRequest.establishDate().atStartOfDay()
                    );
    }
}
