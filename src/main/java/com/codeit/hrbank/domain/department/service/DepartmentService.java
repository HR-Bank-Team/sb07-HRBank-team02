package com.codeit.hrbank.domain.department.service;

import com.codeit.hrbank.domain.department.dto.CursorPageRequestDepartmentDto;
import com.codeit.hrbank.domain.department.dto.DepartmentCreateRequest;
import com.codeit.hrbank.domain.department.dto.DepartmentDto;
import com.codeit.hrbank.domain.department.entity.Department;
import com.codeit.hrbank.domain.department.mapper.DepartmentMapper;
import com.codeit.hrbank.domain.department.repository.DepartmentRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DepartmentService {
  private final DepartmentRepository departmentRepository;
  private final DepartmentMapper departmentMapper;

  //부서 생성
  public DepartmentDto createDepartment(DepartmentCreateRequest request){
    if(departmentRepository.existsByName(request.name())){
      throw new RuntimeException("중복된 부서명입니다.");
    }
    Department department = new Department(request.name(), request.description(), LocalDateTime.now());
    return departmentMapper.toDto(departmentRepository.save(department));
  }
  
  //부서 목록 조회
  public Slice<DepartmentDto> getAllDepartments(CursorPageRequestDepartmentDto request){
    validateSortField(request.sortField());

    // Pageable + 기본 tie-breaker
    Pageable pageable = PageRequest.of(0, request.size(),
        Sort.by(request.sortDirection(), request.sortField())
            .and(Sort.by(request.sortDirection(), "id")));

    // cursor 값 그대로 사용
    String cursorValue = request.cursor();

    // repository 호출
    Slice<Department> departmentSlice = departmentRepository.searchByKeywordWithCursor(
      request.nameOrDescription(),
      cursorValue,
      request.idAfter(),
      request.sortField(),
      pageable
    );

    List<DepartmentDto> departmentDtoList =
        departmentSlice.stream().map(departmentMapper::toDto).toList();

    return new SliceImpl<>(departmentDtoList, pageable, departmentSlice.hasNext());
  }

  // 정렬 필드 검증 메서드 분리
  private void validateSortField(String sortField) {
    if (sortField == null) return;

    if (!sortField.equals("name") && !sortField.equals("establishedDate")) {
      throw new RuntimeException("정렬 기준은 null, name, establishedDate 중 하나여야 합니다.");
    }
  }
}
