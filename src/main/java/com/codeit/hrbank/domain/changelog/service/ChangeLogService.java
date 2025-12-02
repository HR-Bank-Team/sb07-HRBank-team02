package com.codeit.hrbank.domain.changelog.service;

import com.codeit.hrbank.domain.changelog.dto.ChangeLogDto;
import com.codeit.hrbank.domain.changelog.dto.ChangeLogFilter;
import com.codeit.hrbank.domain.changelog.dto.CursorPageResponseChangeLogDto;
import com.codeit.hrbank.domain.changelog.dto.DiffDto;
import com.codeit.hrbank.domain.changelog.entity.ChangeLog;
import com.codeit.hrbank.domain.changelog.entity.ChangeLogType;
import com.codeit.hrbank.domain.changelog.entity.Diff;
import com.codeit.hrbank.domain.changelog.mapper.ChangeLogMapper;
import com.codeit.hrbank.domain.changelog.repository.ChangeLogRepository;
import com.codeit.hrbank.domain.changelog.repository.DiffRepository;
import com.codeit.hrbank.domain.employee.entity.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ChangeLogService {

    private final ChangeLogRepository changeLogRepository;
    private final DiffRepository diffRepository;

    @Transactional
    public void recordLogByAddEmployee(
            String employeeNumber,
            String ipAddress,
            String memo,
            Employee employee) {
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
            List<DiffDto> diffDtos) {
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
            Employee employee) {
        ChangeLog changeLog = new ChangeLog(ChangeLogType.DELETED, ipAddress, LocalDateTime.now(), ChangeLogType.DELETED.getValue(), employeeNumber);
        ChangeLog savedLog = changeLogRepository.save(changeLog);

        List<Diff> diffs = extractDetailsByDelete(employee, savedLog);
        diffRepository.saveAll(diffs);
    }

    @Transactional
    public CursorPageResponseChangeLogDto getChangeLogs(ChangeLogFilter request) {
        List<ChangeLog> changeLogs = changeLogRepository.searchChangeLogs(
                request.getEmployeeNumber(),
                request.getMemo(),
                request.getIpAddress(),
                request.getAtFrom(),
                request.getAtTo(),
                request.getType(),
                request.getCursor(),
                request.getSortField(),
                request.getSortDirection(),
                request.getIdAfter(),
                request.getSize()+1);

        System.out.println(changeLogs.size());
        boolean hasNext = changeLogs.size() > request.getSize();
        int endIndex = Math.min(changeLogs.size(), request.getSize());
        List<ChangeLog> result = changeLogs.subList(0, endIndex);
        ChangeLog last = result.isEmpty() ? null : result.get(result.size() - 1);

        String nextCursor = null;
        Long nextIdAfter = null;

        if (request.getSortField().equals("at") && last != null) {
            nextCursor = last.getAt().atZone(ZoneId.systemDefault()).toInstant().toString();
            nextIdAfter = last.getId();
        } else if (request.getSortField().equals("ipAddress") && last != null) {
            nextCursor = last.getIpAddress();
            nextIdAfter = last.getId();
        }

        Long totalElements = changeLogRepository.countChangeLogs(
                request.getEmployeeNumber(),
                request.getMemo(),
                request.getIpAddress(),
                request.getAtFrom(),
                request.getAtTo(),
                request.getType());

        List<ChangeLogDto> list = result.stream()
                .map(ChangeLogMapper::toDto)
                .toList();
        return new CursorPageResponseChangeLogDto(list, nextCursor, nextIdAfter, list.size(), totalElements, hasNext);


    }


    private List<Diff> extractDetailsByAdd(Employee employee, ChangeLog changeLog) {
        List<Diff> result = new ArrayList<>();
        result.add(new Diff("입사일", null, employee.getHireDate().toString(), changeLog));
        result.add(new Diff("이름", null, employee.getName(), changeLog));
        result.add(new Diff("직함", null, employee.getPosition(), changeLog));
        result.add(new Diff("부서명", null, employee.getDepartment().getName(), changeLog));
        result.add(new Diff("이메일", null, employee.getEmail(), changeLog));
        result.add(new Diff("사번", null, employee.getEmployeeNumber(), changeLog));
        result.add(new Diff("상태", null, employee.getStatus().toString(), changeLog));
        return result;
    }

    private List<Diff> extractDetailsByDelete(Employee employee, ChangeLog changeLog) {
        List<Diff> result = new ArrayList<>();
        result.add(new Diff("입사일",  employee.getHireDate().toString(),null, changeLog));
        result.add(new Diff("이름",  employee.getName(),null, changeLog));
        result.add(new Diff("직함",  employee.getPosition(),null, changeLog));
        result.add(new Diff("부서명",  employee.getDepartment().getName(),null, changeLog));
        result.add(new Diff("이메일",  employee.getEmail(),null, changeLog));
        result.add(new Diff("상태",  employee.getStatus().toString(),null, changeLog));
        return result;
    }
}
