package com.codeit.hrbank.domain.department.repository;

import com.codeit.hrbank.domain.department.entity.Department;
import com.codeit.hrbank.domain.department.projection.DepartmentWithCountEmployee;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    /**
     * 이름 또는 설명 부분 검색 + 안전한 커서 페이지네이션 + 정렬 정렬 기준: name 또는 establishedDate
     */
    // 부서 관리 목록에서 부서명이나 설명으로 키워드를 검색하고, Sort에 따라, 정렬해주는 쿼리.
    // 서비스 단에서 쓸 때 => Pageable pageable = PageRequest.of(0, 5, Sort.by("name").ascending());
    // NOTE: 프로젝션 사용 매핑을 위해 같은 이름이더래도 AS 사용
    @Query("""
                SELECT d.id AS id, d.name AS name, d.description AS description, d.establishedDate AS establishedDate, count(e) AS employeeCount FROM Department d
                LEFT JOIN Employee e on e.department = d
                WHERE (:keyword IS NULL OR d.name LIKE %:keyword% OR d.description LIKE %:keyword%)
                AND (
                    :lastValue IS NULL OR
                    (:sortField = 'name' AND (d.name > :lastValue OR (d.name = :lastValue AND d.id > :lastId))) OR
                    (:sortField = 'establishedDate' AND (d.establishedDate > CAST(:lastValue AS java.time.LocalDate) OR 
                          (d.establishedDate = CAST(:lastValue AS java.time.LocalDate) AND d.id > :lastId)))
                )
                GROUP BY d.id, d.name, d.description, d.establishedDate
            """)
    Slice<DepartmentWithCountEmployee> searchByKeywordWithCursor(
            @Param("keyword") String keyword,
            @Param("lastValue") String lastValue, // 정렬 컬럼 마지막 값
            @Param("lastId") Long lastId,         // 마지막 id
            @Param("sortField") String sortField, // 'name' 또는 'establishedDate'
            Pageable pageable);

    boolean existsByName(String name);
}
