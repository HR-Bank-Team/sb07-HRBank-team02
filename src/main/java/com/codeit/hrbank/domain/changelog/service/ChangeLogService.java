package com.codeit.hrbank.domain.changelog.service;

import com.codeit.hrbank.domain.changelog.dto.*;
import com.codeit.hrbank.domain.changelog.entity.ChangeLog;
import com.codeit.hrbank.domain.changelog.entity.ChangeLogType;
import com.codeit.hrbank.domain.changelog.entity.Diff;
import com.codeit.hrbank.domain.changelog.mapper.ChangeLogMapper;
import com.codeit.hrbank.domain.changelog.mapper.DiffMapper;
import com.codeit.hrbank.domain.changelog.repository.ChangeLogRepository;
import com.codeit.hrbank.domain.changelog.repository.DiffRepository;
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
            CreateLogDetailCommand dtoCommand) {
        ChangeLog changeLog = new ChangeLog(ChangeLogType.CREATED, ipAddress, LocalDateTime.now(), memo, employeeNumber);
        ChangeLog savedLog = changeLogRepository.save(changeLog);

        List<Diff> result = new ArrayList<>();
        result.add(new Diff("입사일", null, dtoCommand.getHireDate(), savedLog));
        result.add(new Diff("이름", null, dtoCommand.getName(), savedLog));
        result.add(new Diff("직함", null, dtoCommand.getPosition(), savedLog));
        result.add(new Diff("부서명", null, dtoCommand.getDepartment(), savedLog));
        result.add(new Diff("이메일", null, dtoCommand.getEmail(), savedLog));
        result.add(new Diff("사번", null, dtoCommand.getEmployeeNumber(), savedLog));
        result.add(new Diff("상태", null, dtoCommand.getStatus(), savedLog));
        diffRepository.saveAll(result);

    }

    @Transactional
    public void recordLogByUpdateEmployee(
            String employeeNumber,
            String ipAddress,
            String memo,
            List<DiffCommand> diffCommands) {
        ChangeLog changeLog =
                new ChangeLog(ChangeLogType.UPDATED, ipAddress, LocalDateTime.now(), memo, employeeNumber);
        ChangeLog savedLog = changeLogRepository.save(changeLog);

        List<Diff> diffs = new ArrayList<>();
        for (DiffCommand diffCommand : diffCommands) {
            diffs.add(new Diff(diffCommand.getPropertyName(), diffCommand.getBefore(), diffCommand.getAfter(), savedLog));
        }
        diffRepository.saveAll(diffs);
    }

    @Transactional
    public void recordLogByDeleteEmployee(
            String employeeNumber,
            String ipAddress,
            DeleteLogDetailCommand dtoCommand) {
        ChangeLog changeLog = new ChangeLog(ChangeLogType.DELETED, ipAddress, LocalDateTime.now(), ChangeLogType.DELETED.getValue(), employeeNumber);
        ChangeLog savedLog = changeLogRepository.save(changeLog);

        List<Diff> result = new ArrayList<>();
        result.add(new Diff("입사일",  dtoCommand.getHireDate(), null, savedLog));
        result.add(new Diff("이름",  dtoCommand.getName(), null, savedLog));
        result.add(new Diff("직함",  dtoCommand.getPosition(), null,savedLog));
        result.add(new Diff("부서명",  dtoCommand.getDepartment(), null,savedLog));
        result.add(new Diff("이메일",  dtoCommand.getEmail(),null, savedLog));
        result.add(new Diff("상태", dtoCommand.getStatus(), null,savedLog));
        diffRepository.saveAll(result);
    }

    @Transactional(readOnly = true)
    public CursorPageResponseChangeLogDto getChangeLogs(ChangeLogFilter request) {
        List<ChangeLog> changeLogs = changeLogRepository.searchChangeLogs(
                request.getEmployeeNumber(),
                request.getMemo(),
                request.getIpAddress(),
                request.getAtFrom()==null?null: request.getAtFrom().toLocalDateTime(),
                request.getAtTo()==null?null: request.getAtTo().toLocalDateTime(),
                request.getType(),
                request.getCursor(),
                request.getSortField(),
                request.getSortDirection(),
                request.getIdAfter(),
                request.getSize() + 1);

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
                request.getAtFrom()==null?null: request.getAtFrom().toLocalDateTime(),
                request.getAtTo()==null?null: request.getAtTo().toLocalDateTime(),
                request.getType());

        List<ChangeLogDto> list = result.stream()
                .map(ChangeLogMapper::toDto)
                .toList();
        return new CursorPageResponseChangeLogDto(list, nextCursor, nextIdAfter, list.size(), totalElements, hasNext);


    }

    @Transactional(readOnly = true)
    public List<DiffDto> getDiffsByChannelLogId(Long changeLogId) {
        if (!changeLogRepository.existsChangeLogById(changeLogId)) {
            throw new IllegalArgumentException("존재하지 않는 채널 수정 이력입니다.");
        }
        List<Diff> byChangeLogId = diffRepository.findByChangeLogId(changeLogId);
        return byChangeLogId.stream().map(DiffMapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public Long countChangeLogsBetween(
            LocalDateTime fromDate,
            LocalDateTime toDate) {
        if (fromDate == null) {
            fromDate = LocalDateTime.now().minusDays(7);
        }
        if (toDate == null) {
            toDate = LocalDateTime.now();
        }
        return changeLogRepository.countByAtBetween(fromDate, toDate);
    }

}
