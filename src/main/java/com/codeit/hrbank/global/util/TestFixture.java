package com.codeit.hrbank.global.util;

import com.codeit.hrbank.domain.changelog.entity.ChangeLog;
import com.codeit.hrbank.domain.changelog.entity.ChangeLogType;
import com.codeit.hrbank.domain.department.entity.Department;
import com.codeit.hrbank.domain.employee.entity.Employee;
import com.codeit.hrbank.domain.employee.entity.EmployeeStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TestFixture {

    public ChangeLog changeLogFactory(){

        return new ChangeLog(
                randomEnumType(ChangeLogType.class),
                randomString(),
                randomLocalDateTime(),
                randomString(),
                randomString()
        );

    }

    public Employee employeeFactory(Department department){
        return new Employee(
                randomString(),
                randomString(),
                randomString(),
                randomLocalDateTime(),
                randomString(),
                randomEnumType(EmployeeStatus.class),
                null,
                department
        );
    }

    public Department departmentFactory(){
        return new Department(
                randomString(),
                randomString(),
                randomLocalDateTime()
        );
    }

    private String randomString(){

        return UUID.randomUUID().toString();
    }

    private LocalDateTime randomLocalDateTime(){
        int randomDay = (int)(Math.random()*365);
        return LocalDateTime.now().minus(Duration.ofDays(randomDay));
    }

    private   <T extends Enum<T>> T randomEnumType(Class<T> enumClass){
        T[] values = enumClass.getEnumConstants();
        return values[(int)(Math.random()*values.length)];
    }

}
