package com.codeit.hrbank.domain.employee.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.Instant;

@Table(name = "employees")
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long employeeId;

    @Column(name = "name",nullable = false)
    private String name;

    @Column(name = "position",nullable = false)
    private String position;

    @Column(name= "email",nullable = false, unique=true)
    private String email;

    @CreatedDate
    @Column(name ="hire_date",nullable = false)
    private Instant hireDate;


    @Column(name = "employee_number",nullable = false,unique = true)
    private String employeeNumber;


    @Enumerated(EnumType.STRING)
    @Column(name = "status",nullable = false)
    private EmployeeStatus status;

    @CreatedDate
    @Column(name = "created_at",nullable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;

}
