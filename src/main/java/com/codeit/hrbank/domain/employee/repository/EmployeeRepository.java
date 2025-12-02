package com.codeit.hrbank.domain.employee.repository;

import com.codeit.hrbank.domain.employee.entity.Employee;
import com.codeit.hrbank.domain.employee.entity.EmployeeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    boolean existsByEmail(String email);

    // 직원 수 조회 (상태 + 입사일)
    @Query("select count(e) " +
            "from Employee e " +
            "where (:status is null or e.status = :status) " +
            "and (:start is null or e.hireDate >= :start) " +
            "and (:end is null or e.hireDate <= :end)")
    long countByStatusAndHireDateBetween(
            @Param("status") EmployeeStatus status,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    //부서별 직원 분포
    @Query("select d.name as groupKey, count(e) as count " +
            "from Employee e join e.department d " +
            "where (:status is null or e.status = :status) " +
            "group by d.name")
    List<EmployeeGroupCount> countGroupByDepartment(
            @Param("status") EmployeeStatus status
    );

    //직무별 직원 수
    @Query("select e.position as groupKey, count(e) as count " +
            "from Employee e " +
            "where (:status is null or e.status = :status) " +
            "group by e.position")
    List<EmployeeGroupCount> countGroupByPosition(
            @Param("status") EmployeeStatus status
    );

    //분포 조회용
    interface EmployeeGroupCount {
        String getGroupKey();
        long getCount();
    }
}
