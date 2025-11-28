package com.codeit.hrbank.domain.employee.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;

@Table(name = "employees")
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "employee_id")
    private Long id;

    @Column(name = "name",nullable = false,length = 100)
    private String name;

    @Column(name = "position",nullable = false,length = 100)
    private String position;

    @Column(name= "email",nullable = false, unique=true,length = 100)
    private String email;

    @Column(name ="hire_date",nullable = false)
    private LocalDateTime hireDate;


    @Column(name = "employee_number",nullable = false,unique = true)
    private String employeeNumber;


    @Enumerated(EnumType.STRING)
    @Column(name = "status",nullable = false,length = 100)
    private EmployeeStatus status;

    @CreatedDate
    @Column(name = "created_at",nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
