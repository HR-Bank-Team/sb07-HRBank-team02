package com.codeit.hrbank.domain.changelog.service;

import com.codeit.hrbank.domain.changelog.dto.ChangeLogDto;
import com.codeit.hrbank.domain.changelog.dto.ChangeLogFilter;
import com.codeit.hrbank.domain.changelog.dto.CursorPageResponseChangeLogDto;
import com.codeit.hrbank.domain.changelog.dto.DiffDto;
import com.codeit.hrbank.domain.changelog.entity.ChangeLog;
import com.codeit.hrbank.domain.changelog.entity.ChangeLogType;
import com.codeit.hrbank.domain.changelog.entity.Diff;
import com.codeit.hrbank.domain.changelog.mapper.ChangeLogMapper;
import com.codeit.hrbank.domain.changelog.mapper.DiffMapper;
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
import java.util.Map;


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
            Map<String, List<String>> diffMapData) {
        ChangeLog changeLog =
                new ChangeLog(ChangeLogType.UPDATED, ipAddress, LocalDateTime.now(), memo, employeeNumber);
        ChangeLog savedLog = changeLogRepository.save(changeLog);

        List<Diff> diffs = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : diffMapData.entrySet()) {
            diffs.add(new Diff(entry.getKey(), entry.getValue().get(0), entry.getValue().get(0), savedLog));
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

    @Transactional(readOnly = true)
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

        Long totalElements = changeLogRepository.countChangeLogsByFilter(
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
    @Transactional(readOnly = true)
    public List<DiffDto> getDiffsByChannelLogId(Long changeLogId){
        if (!changeLogRepository.existsChangeLogById(changeLogId)){
            throw new IllegalArgumentException("존재하지 않는 채널 수정 이력입니다.");
        }
        List<Diff> byChangeLogId = diffRepository.findByChangeLogId(changeLogId);
        return byChangeLogId.stream().map(DiffMapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public Long countChangeLogsBetween(
            LocalDateTime fromDate,
            LocalDateTime toDate){
        if(fromDate==null){
            fromDate = LocalDateTime.now().minusDays(7);
        }
        if(toDate==null){
            toDate=LocalDateTime.now();
        }
        return changeLogRepository.countByAtBetween(fromDate,toDate);
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
