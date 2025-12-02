package com.codeit.hrbank.domain.employee.repository;

import com.codeit.hrbank.domain.employee.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Long> {
    boolean existsByEmail(String email);

    long countByDepartmentId(Long departmentId);

    // targetDate까지 입사한 직원의 수 구하기
    @Query(value = """
        select count(e)
        from Employee e
        where e.hireDate <= :targetDate
        """)
    int findEmployeeTrend(LocalDate targetDate);
}
