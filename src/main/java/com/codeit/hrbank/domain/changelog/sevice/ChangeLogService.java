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


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ChangeLogService {

    private ChangeLogRepository changeLogRepository;
    private DiffRepository diffRepository;

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

    public void recordLogByDeleteEmployee(
            String employeeNumber,
            String ipAddress,
            Employee employee){
        ChangeLog changeLog = new ChangeLog(ChangeLogType.DELETED, ipAddress, LocalDateTime.now(), ChangeLogType.DELETED.getValue(), employeeNumber);
        ChangeLog savedLog = changeLogRepository.save(changeLog);

        List<Diff> diffs = extractDetailsByDelete(employee, savedLog);
        diffRepository.saveAll(diffs);
    }


//    **이력 목록 조회**
//   - **{대상 직원 사번}**, **{메모}**, **{IP 주소}**, **{시간}, {유형}**으로 이력 목록을 조회할 수 있습니다.
//            - **{대상 직원 사번}**, **{메모}**, **{IP 주소}**은 부분 일치 조건입니다.
//            - **{시간}**은 범위 조건입니다.
//    - **{유형}**은 완전 일치 조건입니다.
//            - 조회 조건이 여러 개인 경우 모든 조건을 만족한 결과로 조회합니다.
//            - **{IP 주소}**, **{시간}**으로 정렬 및 페이지네이션을 구현합니다.
//    - 여러 개의 정렬 조건 중 선택적으로 1개의 정렬 조건만 가질 수 있습니다.
//            - 정확한 페이지네이션을 위해 **{이전 페이지의 마지막 요소 ID}**를 활용합니다.
//            - 화면을 고려해 적절한 페이지네이션 전략을 선택합니다.
//            - 데이터 크기를 고려, **{변경 상세 내용}**은 포함하지 않습니다.


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
