package com.codeit.hrbank.domain.employee.repository;

import com.codeit.hrbank.domain.employee.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Long> {
    boolean existsByEmail(String email);

    Long countByDepartmentId(Long departmentId);
}
