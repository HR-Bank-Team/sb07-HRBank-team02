package com.codeit.hrbank.domain.employee.mapper;


import com.codeit.hrbank.domain.department.entity.Department;
import com.codeit.hrbank.domain.employee.dto.CursorPageResponseEmployeeDto;
import com.codeit.hrbank.domain.employee.dto.EmployeeDto;
import com.codeit.hrbank.domain.employee.entity.Employee;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EmployeeMapper {

    public EmployeeDto toDto(Employee employee) {
        Department department = employee.getDepartment();

        // 프로필이 없는 경우 null 저장
        Long profileId = employee.getProfile() != null ? employee.getProfile().getId() : null;


        return new EmployeeDto(
                employee.getId(),
                employee.getName(),
                employee.getEmail(),
                employee.getEmployeeNumber(),
                department.getId(),
                department.getName(),
                employee.getPosition(),
                employee.getHireDate(),
                employee.getStatus(),
                profileId
        );
    }

    public List<EmployeeDto> toDto(List<Employee> employees) {
        return employees.stream()
                .map(employee -> {
                    Department department = employee.getDepartment();

                    // 프로필이 없는 경우 null 저장
                    Long profileId = employee.getProfile() != null ? employee.getProfile().getId() : null;

                    return new EmployeeDto(
                            employee.getId(),
                            employee.getName(),
                            employee.getEmail(),
                            employee.getEmployeeNumber(),
                            department.getId(),
                            department.getName(),
                            employee.getPosition(),
                            employee.getHireDate(),
                            employee.getStatus(),
                            profileId
                    );
                })
                .toList();
    }

    public CursorPageResponseEmployeeDto toDto(Slice<EmployeeDto> employeeSlice, Long count) {
        // 검색 결과가 없는 경우 전달
        // 최적화 가능한 경우 코드 변경 예정
        if (employeeSlice.getContent().isEmpty()) {
            return new CursorPageResponseEmployeeDto(
                    new ArrayList(),
                    null,
                    null,
                    0,
                    0,
                    false
            );
        }

        EmployeeDto lastEmployee = employeeSlice.getContent().get(employeeSlice.getContent().size() - 1);
        Sort sort = employeeSlice.getSort();
        String property = sort.iterator().next().getProperty(); // 정렬 값을 하나만 사용하므로
        Long nextIdAfter = lastEmployee.id();
        String nextCursor = null;
        Integer totalElements = count.intValue(); // 조건에 맞는 총 인원수 Integer로 변환

        // 정렬 값에 따라 커서 값 저장
        switch(property) {
            case "name" -> nextCursor = lastEmployee.name();
            case "hireDate" -> nextCursor = lastEmployee.hireDate().toString();
            case "employeeNumber" -> nextCursor = lastEmployee.employeeNumber();
        }

        return new CursorPageResponseEmployeeDto(
                employeeSlice.getContent(),
                nextCursor,
                nextIdAfter,
                employeeSlice.getSize(),
                totalElements,
                employeeSlice.hasNext()
        );
    }
}
