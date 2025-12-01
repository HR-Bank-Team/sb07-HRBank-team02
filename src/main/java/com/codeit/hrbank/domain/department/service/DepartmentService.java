package com.codeit.hrbank.domain.department.service;

import com.codeit.hrbank.domain.department.dto.CursorPageRequestDepartmentDto;
import com.codeit.hrbank.domain.department.dto.CursorPageResponseDepartmentDto;
import com.codeit.hrbank.domain.department.dto.DepartmentCreateRequest;
import com.codeit.hrbank.domain.department.dto.DepartmentDto;
import com.codeit.hrbank.domain.department.entity.Department;
import com.codeit.hrbank.domain.department.mapper.DepartmentMapper;
import com.codeit.hrbank.domain.department.repository.DepartmentRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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
      throw new IllegalStateException("중복된 부서명입니다.");
    }
    Department department = departmentMapper.toEntity(request);
    return departmentMapper.toDto(departmentRepository.save(department));
  }
  
  //부서 목록 조회
  public CursorPageResponseDepartmentDto getAllDepartments(CursorPageRequestDepartmentDto request){
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

    List<DepartmentDto> content =
        departmentSlice.stream().map(departmentMapper::toDto).toList();

    // nextCursor, nextIdAfter 계산
    String nextCursor = null;
    Long nextIdAfter = null;

    if (departmentSlice.hasNext() && !content.isEmpty()) {
      DepartmentDto last = content.get(content.size() - 1);

      // 커서 스트링은 정렬 필드 값으로 생성한다고 가정
      nextCursor = switch (request.sortField()) {
        case "name" -> last.name();
        case "establishedDate" -> last.establishedDate().toString();
        default -> last.id().toString();  // 혹시 모를 fallback
      };

      nextIdAfter = last.id();
    }

    return new CursorPageResponseDepartmentDto(
        content,
        nextCursor,
        nextIdAfter,
        request.size(),
        (long) content.size(),       // totalElements (cursor 방식에서는 보통 count 안함)
        departmentSlice.hasNext()
    );
  }

  // 정렬 필드 검증 메서드 분리
  private void validateSortField(String sortField) {
    if (!sortField.equals("name") && !sortField.equals("establishedDate")) {
      throw new IllegalArgumentException("정렬 기준은 null, name, establishedDate 중 하나여야 합니다.");
    }
  }
}
