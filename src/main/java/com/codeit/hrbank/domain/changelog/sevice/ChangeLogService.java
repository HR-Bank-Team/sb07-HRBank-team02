package com.codeit.hrbank.domain.changelog.sevice;

import com.codeit.hrbank.domain.changelog.dto.DiffDto;
import com.codeit.hrbank.domain.changelog.entity.ChangeLog;
import com.codeit.hrbank.domain.changelog.entity.Diff;
import com.codeit.hrbank.domain.changelog.entity.ChangeLogType;
import com.codeit.hrbank.domain.changelog.repository.DiffRepository;
import com.codeit.hrbank.domain.changelog.repository.ChangeLogRepository;
import com.codeit.hrbank.domain.employee.entity.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ChangeLogService {

    private ChangeLogRepository changeLogRepository;
    private DiffRepository diffRepository;

    @Transactional
    public void recordLogByAddEmployee(
            String employeeNumber,
            String ipAddress,
            String memo,
            Employee employee){
        ChangeLog changeLog = new ChangeLog(ChangeLogType.CREATED, ipAddress, LocalDateTime.now(), memo, employeeNumber);
        ChangeLog savedLog = changeLogRepository.save(changeLog);

        List<Diff> diffs = extractDetailsByAdd(employee, savedLog);
        diffRepository.saveAll(diffs);

    }

    @Transactional
    public void recordLogByUpdateEmployee(
            String employeeNumber,
            String ipAddress,
            String memo,
            List<DiffDto> diffDtos){
        ChangeLog changeLog =
                new ChangeLog(ChangeLogType.UPDATED, ipAddress, LocalDateTime.now(), memo, employeeNumber);
        ChangeLog savedLog = changeLogRepository.save(changeLog);

        List<Diff> diffs = new ArrayList<>();
        for (DiffDto diffDto : diffDtos) {
            diffs.add(new Diff(diffDto.toString(), diffDto.getBefore(), diffDto.getAfter(), savedLog));
        }
        diffRepository.saveAll(diffs);
    }

    @Transactional
    public void recordLogByDeleteEmployee(
            String employeeNumber,
            String ipAddress,
            Employee employee){
        ChangeLog changeLog = new ChangeLog(ChangeLogType.DELETED, ipAddress, LocalDateTime.now(), ChangeLogType.DELETED.getValue(), employeeNumber);
        ChangeLog savedLog = changeLogRepository.save(changeLog);

        List<Diff> diffs = extractDetailsByDelete(employee, savedLog);
        diffRepository.saveAll(diffs);
    }

    public void getChangeLogsSorted(){

    }


    private List<Diff> extractDetailsByAdd(Employee employee, ChangeLog changeLog) {
        List<Diff> result = new ArrayList<>();
        result.add(new Diff("입사일",null, employee.getHireDate().toString(), changeLog));
        result.add(new Diff("이름",null, employee.getName(), changeLog));
        result.add(new Diff("직함",null, employee.getPosition(), changeLog));
        result.add(new Diff("부서명",null, employee.getDepartment().getName(), changeLog));
        result.add(new Diff("이메일",null, employee.getEmail(), changeLog));
        result.add(new Diff("사번",null, employee.getEmployeeNumber(), changeLog));
        result.add(new Diff("상태",null, employee.getStatus().toString(), changeLog));
        return result;
    }

    private List<Diff> extractDetailsByDelete(Employee employee, ChangeLog changeLog) {
        List<Diff> result = new ArrayList<>();
        result.add(new Diff("입사일",null, employee.getHireDate().toString(), changeLog));
        result.add(new Diff("이름",null, employee.getName(), changeLog));
        result.add(new Diff("직함",null, employee.getPosition(), changeLog));
        result.add(new Diff("부서명",null, employee.getDepartment().getName(), changeLog));
        result.add(new Diff("이메일",null, employee.getEmail(), changeLog));
        result.add(new Diff("상태",null, employee.getStatus().toString(), changeLog));
        return result;
    }
}
