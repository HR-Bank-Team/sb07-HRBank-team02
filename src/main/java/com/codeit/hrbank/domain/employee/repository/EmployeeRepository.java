package com.codeit.hrbank.domain.employee.repository;

import com.codeit.hrbank.domain.employee.entity.Employee;
import com.codeit.hrbank.domain.employee.entity.EmployeeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    boolean existsByEmail(String email);

    // 직원 수 조회 (LocalDate 기반)
    long countByStatusAndHireDateBetween(
            EmployeeStatus status,
            LocalDate fromDate,
            LocalDate toDate
    );

    // ✅ 2. 기간만으로 직원 수 조회 (status가 없을 때)
    long countByHireDateBetween(
            LocalDate fromDate,
            LocalDate toDate
    );
    // 직원 분포 조회 (부서별)
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
}
