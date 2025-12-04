package com.codeit.hrbank.domain.department.service;

import com.codeit.hrbank.domain.department.dto.*;
import com.codeit.hrbank.domain.department.entity.Department;
import com.codeit.hrbank.domain.department.mapper.DepartmentMapper;
import com.codeit.hrbank.domain.department.projection.DepartmentWithCountEmployee;
import com.codeit.hrbank.domain.department.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    //부서 생성
    @Transactional
    public DepartmentDto createDepartment(DepartmentCreateRequest request) {
        if (departmentRepository.existsByName(request.name())) {
            throw new IllegalStateException("중복된 부서명입니다.");
        }
        Department department = departmentMapper.toEntity(request);
        return departmentMapper.toDto(departmentRepository.save(department));
    }

    //부서 단일 조회
    @Transactional(readOnly = true)
    public DepartmentDto getDepartment(Long departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new NoSuchElementException("해당 ID 를 가진 부서가 없습니다."));
        return departmentMapper.toDto(department);
    }

    //부서 목록 조회
    @Transactional(readOnly = true)
    public CursorPageResponseDepartmentDto getAllDepartments(CursorPageRequestDepartmentDto request) {
      validateSortField(request.sortField());
      Pageable pageable = request.toPageable();
      long totalCount = departmentRepository.countByKeyword(request.nameOrDescription());

        // repository 호출
        Slice<DepartmentWithCountEmployee> departmentSlice = departmentRepository.searchByKeywordWithCursor(
                request.nameOrDescription(),
                request.cursor(),
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
                totalCount,
                departmentSlice.hasNext()
        );
    }

    @Transactional
    public void deleteDepartment(Long departmentId) {
        departmentRepository.deleteById(departmentId);
    }

    @Transactional
    public DepartmentDto updateDepartment(Long departmentId, DepartmentUpdateRequest request) {
        Optional.ofNullable(departmentId).orElseThrow(() -> new IllegalArgumentException("id 값이 잘못되었습니다."));

        Department department = departmentRepository.findById(departmentId).orElseThrow(() -> new IllegalArgumentException("존재하지않는 값입니다"));
        department.update(request.name(), request.description(), request.establishedDate());
        Department saved = departmentRepository.save(department);

        return departmentMapper.toDto(saved);

    }

    // 정렬 필드 검증 메서드 분리
    private void validateSortField(String sortField) {
      if(sortField == null){
        sortField = "establishedDate";
      }
      switch (sortField) {
        case "name" :
        case "establishedDate" :
          break;
        default:
          throw new IllegalArgumentException("정렬 기준은 null, name, establishedDate 중 하나여야 합니다.");
      }
    }
}
