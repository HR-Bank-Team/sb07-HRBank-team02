package com.codeit.hrbank.domain.changelog.sevice;

import com.codeit.hrbank.domain.changelog.dto.UpdatedFieldDetail;
import com.codeit.hrbank.domain.changelog.entity.ChangeLog;
import com.codeit.hrbank.domain.changelog.entity.ChangeLogDetail;
import com.codeit.hrbank.domain.changelog.entity.ChangeLogType;
import com.codeit.hrbank.domain.changelog.repository.ChangeLogDetailRepository;
import com.codeit.hrbank.domain.changelog.repository.ChangeLogRepository;
import com.codeit.hrbank.domain.employee.entity.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChangeLogService {

    private ChangeLogRepository changeLogRepository;
    private ChangeLogDetailRepository changeLogDetailRepository;

    public void recordLogByAddEmployee(
            String employeeNumber,
            String ipAddress,
            String memo,
            Employee employee){
        ChangeLog changeLog = new ChangeLog(ChangeLogType.CREATED, ipAddress, LocalDateTime.now(), memo, employeeNumber);
        ChangeLog savedLog = changeLogRepository.save(changeLog);

        List<ChangeLogDetail> changeLogDetails = extractDetailsByAdd(employee, savedLog);
        changeLogDetailRepository.saveAll(changeLogDetails);

    }

    public void recordLogByUpdateEmployee(
            String employeeNumber,
            String ipAddress,
            String memo,
            List<UpdatedFieldDetail> updatedFieldDetails){
        ChangeLog changeLog =
                new ChangeLog(ChangeLogType.UPDATED, ipAddress, LocalDateTime.now(), memo, employeeNumber);
        ChangeLog savedLog = changeLogRepository.save(changeLog);

        List<ChangeLogDetail> changeLogDetails = new ArrayList<>();
        for (UpdatedFieldDetail field : updatedFieldDetails) {
            changeLogDetails.add(new ChangeLogDetail(field.getPropertyType().toString(), field.getBefore(), field.getAfter(), savedLog));
        }
        changeLogDetailRepository.saveAll(changeLogDetails);
    }

    public void recordLogByDeleteEmployee(
            String employeeNumber,
            String ipAddress,
            Employee employee){
        ChangeLog changeLog = new ChangeLog(ChangeLogType.DELETED, ipAddress, LocalDateTime.now(), ChangeLogType.DELETED.getValue(), employeeNumber);
        ChangeLog savedLog = changeLogRepository.save(changeLog);

        List<ChangeLogDetail> changeLogDetails = extractDetailsByDelete(employee, savedLog);
        changeLogDetailRepository.saveAll(changeLogDetails);

    }

    private List<ChangeLogDetail> extractDetailsByAdd(Employee employee, ChangeLog changeLog) {
        List<ChangeLogDetail> result = new ArrayList<>();
        result.add(new ChangeLogDetail("입사일",null, employee.getHireDate().toString(), changeLog));
        result.add(new ChangeLogDetail("이름",null, employee.getName(), changeLog));
        result.add(new ChangeLogDetail("직함",null, employee.getPosition(), changeLog));
        result.add(new ChangeLogDetail("부서명",null, employee.getDepartment().getName(), changeLog));
        result.add(new ChangeLogDetail("이메일",null, employee.getEmail(), changeLog));
        result.add(new ChangeLogDetail("사번",null, employee.getEmployeeNumber(), changeLog));
        result.add(new ChangeLogDetail("상태",null, employee.getStatus().toString(), changeLog));
        return result;
    }

    private List<ChangeLogDetail> extractDetailsByDelete(Employee employee, ChangeLog changeLog) {
        List<ChangeLogDetail> result = new ArrayList<>();
        result.add(new ChangeLogDetail("입사일",null, employee.getHireDate().toString(), changeLog));
        result.add(new ChangeLogDetail("이름",null, employee.getName(), changeLog));
        result.add(new ChangeLogDetail("직함",null, employee.getPosition(), changeLog));
        result.add(new ChangeLogDetail("부서명",null, employee.getDepartment().getName(), changeLog));
        result.add(new ChangeLogDetail("이메일",null, employee.getEmail(), changeLog));
        result.add(new ChangeLogDetail("상태",null, employee.getStatus().toString(), changeLog));
        return result;
    }
}
