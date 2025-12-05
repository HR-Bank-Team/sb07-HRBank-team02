package com.codeit.hrbank.domain.employee.entity;

import com.codeit.hrbank.domain.base.BaseUpdatableEntity;
import com.codeit.hrbank.domain.department.entity.Department;
import com.codeit.hrbank.domain.file.entity.File;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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
    private LocalDate hireDate;


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

    public void update(String name, String position, String email, LocalDate hireDate, EmployeeStatus status, File profile, Department department) {
        if(name != null && !name.equals(this.name)) {
            this.name = name;
        }
        if(position != null && !position.equals(this.position)) {
            this.position = position;
        }
        if(email != null && !email.equals(this.email)) {
            this.email = email;
        }
        if(hireDate != null && !hireDate.equals(this.hireDate)) {
            this.hireDate = hireDate;
        }
        if(status != null && !status.equals(this.status)) {
            this.status = status;
        }
        if(profile != null && !profile.equals(this.profile)) {
            this.profile = profile;
        }
        if(department != null && !department.equals(this.department)) {
            this.department = department;
        }
    }


}
