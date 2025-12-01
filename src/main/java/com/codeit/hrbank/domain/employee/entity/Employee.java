package com.codeit.hrbank.domain.employee.entity;

import com.codeit.hrbank.domain.base.BaseUpdatableEntity;
import com.codeit.hrbank.domain.department.entity.Department;
import com.codeit.hrbank.domain.file.entity.File;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "employees")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Employee extends BaseUpdatableEntity {


    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 100)
    private String position;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "hire_date", nullable = false)
    private LocalDateTime hireDate;


    @Column(name = "employee_number", nullable = false, unique = true)
    private String employeeNumber;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 100)
    private EmployeeStatus status;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "profile_id", unique = true)
    private File profile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;


}
