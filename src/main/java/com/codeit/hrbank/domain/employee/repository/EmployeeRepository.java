package com.codeit.hrbank.domain.employee.repository;

import com.codeit.hrbank.domain.employee.dto.EmployeeDto;
import com.codeit.hrbank.domain.employee.entity.Employee;
import com.codeit.hrbank.domain.employee.entity.EmployeeStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    boolean existsByEmail(String email);

    // ============================================================
    // 직원 수 조회 (LocalDate 기반)
    // ============================================================
    @Query("""
        select count(e)
        from Employee e
        where (:status is null or e.status = :status)
          and (:start is null or e.hireDate >= :start)
          and (:end is null or e.hireDate <= :end)
        """)
    long countByStatusAndHireDateBetween(
            @Param("status") EmployeeStatus status,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end
    );

    // ============================================================
    // 직원 분포 조회 (부서별)
    // ============================================================
    @Query("""
        select d.name as groupKey, count(e) as count
        from Employee e join e.department d
        where (:status is null or e.status = :status)
        group by d.name
        """)
    List<EmployeeGroupCount> countGroupByDepartment(
            @Param("status") EmployeeStatus status
    );

    // ============================================================
    // 직원 분포 조회 (직무별)
    // ============================================================
    @Query("""
        select e.position as groupKey, count(e) as count
        from Employee e
        where (:status is null or e.status = :status)
        group by e.position
        """)
    List<EmployeeGroupCount> countGroupByPosition(
            @Param("status") EmployeeStatus status
    );

    // 분포 조회용 Projection
    interface EmployeeGroupCount {
        String getGroupKey();
        long getCount();
    }

    // ============================================================
    // 직원 증감 추이 조회용 (Trend)
    // ============================================================
    @Query("""
        select count(e)
        from Employee e
        where e.hireDate <= :targetDate
        """)
    int findEmployeeTrend(@Param("targetDate") LocalDate targetDate);

    long countByDepartmentId(Long departmentId);


    // 처음 페이지를 가져오는 경우는 cursor도 null이기 때문에 idAfter가 null이더라도 문제 없이 실행
    // idAfter를 활용하여 이름이 중복되는 경우 데이터 누락을 방지
    @Query("""
        select new com.codeit.hrbank.domain.employee.dto.EmployeeDto(
            e.id,
            e.name,
            e.email,
            e.employeeNumber,
            e.department.id,
            e.department.name,
            e.position,
            e.hireDate,
            e.status,
            e.profile.id
        )
        from Employee e
        where
            (:nameOrEmail is null or e.name like %:nameOrEmail% or e.email like %:nameOrEmail%)
        and (:departmentName is null or e.department.name like %:departmentName%)
        and (:position is null or e.position like %:position%)
        and (:employeeNumber is null or e.employeeNumber like %:employeeNumber%)
        and (:hireDateFrom is null or e.hireDate >= :hireDateFrom)
        and (:hireDateTo is null or e.hireDate < :hireDateTo)
        and (:status is null or e.status = :status)
        and (
            (:sortDirection = 'asc' and
                (:sortField = 'name' and (:cursor is null or (e.name > :cursor or (e.name = :cursor and e.id > :idAfter))))
                or (:sortField = 'hireDate' and (:cursor is null or (e.hireDate > CAST(:cursor AS java.time.LocalDate) or (e.hireDate = CAST(:cursor AS java.time.LocalDate) and e.id > :idAfter))))
                or (:sortField = 'employeeNumber' and (:cursor is null or (e.employeeNumber > :cursor or (e.employeeNumber = :cursor and e.id > :idAfter))))
            )
            or
            (:sortDirection = 'desc' and
                (:sortField = 'name' and (:cursor is null or (e.name < :cursor or (e.name = :cursor and e.id > :idAfter))))
                or (:sortField = 'hireDate' and (:cursor is null or (e.hireDate < CAST(:cursor AS java.time.LocalDate) or (e.hireDate = CAST(:cursor AS java.time.LocalDate) and e.id > :idAfter))))
                or (:sortField = 'employeeNumber' and (:cursor is null or (e.employeeNumber < :cursor or (e.employeeNumber = :cursor and e.id > :idAfter))))
            )
        )
    """)
    Slice<EmployeeDto> findByKeywordWithCursor(
            String nameOrEmail,
            String departmentName,
            String position,
            String employeeNumber,
            @Param("hireDateFrom") LocalDate hireDateFrom,
            @Param("hireDateTo") LocalDate hireDateTo,
            EmployeeStatus status,
            String cursor,
            Long idAfter,
            String sortDirection,
            String sortField,
            Pageable pageable
    );

    // 검색 조건에 맞는 total elements를 구하는 쿼리
    // slice는 전체 값을 모르기 때문에 추가
    @Query("""
        select count(e)
        from Employee e
        where
            (:nameOrEmail is null or e.name like %:nameOrEmail% or e.email like %:nameOrEmail%)
        and (:departmentName is null or e.department.name like %:departmentName%)
        and (:position is null or e.position like %:position%)
        and (:employeeNumber is null or e.employeeNumber like %:employeeNumber%)
        and (:hireDateFrom is null or e.hireDate >= :hireDateFrom)
        and (:hireDateTo is null or e.hireDate < :hireDateTo)
        and (:status is null or e.status = :status)
    """)
    long countByKeyword(
            String nameOrEmail,
            String departmentName,
            String position,
            String employeeNumber,
            LocalDate hireDateFrom,
            LocalDate hireDateTo,
            EmployeeStatus status
    );
}
