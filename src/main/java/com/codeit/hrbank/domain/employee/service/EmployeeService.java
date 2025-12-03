package com.codeit.hrbank.domain.employee.service;

import com.codeit.hrbank.domain.changelog.service.ChangeLogService;
import com.codeit.hrbank.domain.department.entity.Department;
import com.codeit.hrbank.domain.department.repository.DepartmentRepository;
import com.codeit.hrbank.domain.employee.dto.*;
import com.codeit.hrbank.domain.employee.entity.Employee;
import com.codeit.hrbank.domain.employee.entity.EmployeeStatus;
import com.codeit.hrbank.domain.employee.mapper.EmployeeMapper;
import com.codeit.hrbank.domain.employee.repository.EmployeeRepository;
import com.codeit.hrbank.domain.file.entity.File;
import com.codeit.hrbank.domain.file.repository.FileRepository;
import com.codeit.hrbank.domain.file.service.FileStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final FileRepository fileRepository;

    private final ChangeLogService changeLogService;

    private final EmployeeMapper employeeMapper;

    private final FileStorage fileStorage;

    // 직원 등록
    @Transactional
    public EmployeeDto createEmployee(EmployeeCreateRequest request, MultipartFile file, String clientIp) throws IOException {
        // 이메일 중복 검증
        if (employeeRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("이미 존재하는 이메일 입니다.");
        }

        Department department = departmentRepository.findById(request.departmentId())
                .orElseThrow(() -> new NoSuchElementException("부서가 존재하지 않습니다."));

        int year = LocalDate.now().getYear();
        String employeeNumber = String.format("EMP-%d-%s", year, UUID.randomUUID());

        File profile = null;

        if (file != null) {
            profile = new File(
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getSize()
            );

            fileRepository.save(profile);
            fileStorage.saveProfile(profile.getId(), file.getBytes());
        }

        Employee newEmployee = new Employee(
                request.name(),
                request.position(),
                request.email(),
                request.hireDate(),
                employeeNumber,
                EmployeeStatus.ACTIVE,
                profile,
                department
        );

        // 직원 생성 로그 저장
        changeLogService.recordLogByAddEmployee(employeeNumber, clientIp, request.memo(), newEmployee);

        employeeRepository.save(newEmployee);
        return employeeMapper.toDto(newEmployee);
    }

    // 직원 상세 조회
    @Transactional(readOnly = true)
    public EmployeeDto getEmployee(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new NoSuchElementException("직원이 존재하지 않습니다."));

        return employeeMapper.toDto(employee);
    }

    // 직원 전체 조회
    @Transactional(readOnly = true)
    public CursorPageResponseEmployeeDto getAllEmployee(CursorPageRequestEmployeeDto request) {

        Direction direction = request.sortDirection() == SortDirection.asc ? Direction.ASC : Direction.DESC;

        // direction과 sortField() 값에 따라 이름, 입사일, 사원번호 중 하나의 기준으로 정렬
        Pageable pageable = PageRequest.of(0, request.size(),
                Sort.by(direction, request.sortField().toString())
        );

        Slice<EmployeeDto> employeeSlice = employeeRepository.findByKeywordWithCursor(
                request.nameOrEmail(),
                request.departmentName(),
                request.position(),
                request.employeeNumber(),
                request.hireDateFrom(),
                request.hireDateTo(),
                request.status(),
                request.cursor(),
                request.idAfter(), // 첫 페이지의 경우 null 값을 저장하여 처음 id부터
                request.sortDirection().toString(),
                request.sortField().toString(),
                pageable
        );

        // 조건에 맞는 총 인원 수 저장
        Long count = employeeRepository.countByKeyword(
                request.nameOrEmail(),
                request.departmentName(),
                request.position(),
                request.employeeNumber(),
                request.hireDateFrom(),
                request.hireDateTo(),
                request.status()
        );

        return employeeMapper.toDto(employeeSlice, count);
    }

    // 직원 정보 수정
    @Transactional
    public EmployeeDto updateEmployee(Long employeeId, EmployeeUpdateRequest request, MultipartFile file, String clientIp) throws IOException {

        // 이메일 검증
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new NoSuchElementException("직원이 존재하지 않습니다."));

        Department department = departmentRepository.findById(request.departmentId())
                .orElseThrow(() -> new NoSuchElementException("부서가 존재하지 않습니다."));

        // 이메일을 변경하지 않은 경우에는 예외 제외
        if (!employee.getEmail().equals(request.email()) && employeeRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("이미 존재하는 이메일 입니다.");
        }


        // 수정 전 직원 정보 저장 (로그용)
        EmployeeDto beforeEmployeeInfo = employeeMapper.toDto(employee);

        File profile = null;

        if (file != null) {
            profile = new File(
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getSize()
            );

            fileRepository.deleteById(employee.getProfile().getId()); // 기존 프로필 이미지 정보 삭제
            // 프로필 이미지 삭제 로직 추가 필요

            fileRepository.save(profile); // 새로운 프로필 이미지 정보 저장
            fileStorage.saveProfile(profile.getId(), file.getBytes()); // 새로운 프로필 이미지 저장
        }

        employee.update(
                request.name(),
                request.position(),
                request.email(),
                request.hireDate(),
                request.status(),
                profile,
                department
        );

        employeeRepository.save(employee);

        // 직원 수정 정보 로그 저장
        Map<String, List<String>> diff = new ConcurrentHashMap<>();

        if(!beforeEmployeeInfo.hireDate().equals(employee.getHireDate())){
            diff.put("입사일", List.of(beforeEmployeeInfo.hireDate().toString(), employee.getHireDate().toString()));
        }
        if(!beforeEmployeeInfo.name().equals(employee.getName())) {
            diff.put("이름", List.of(beforeEmployeeInfo.name(), employee.getName()));
        }
        if(!beforeEmployeeInfo.position().equals(employee.getPosition())) {
            diff.put("직함",List.of(beforeEmployeeInfo.position(), employee.getPosition()));
        }
        if(!beforeEmployeeInfo.departmentId().equals(employee.getDepartment().getId())){
            diff.put("부서명", List.of(beforeEmployeeInfo.departmentName(), employee.getDepartment().getName()));
        }
        if(!beforeEmployeeInfo.email().equals(employee.getEmail())){
            diff.put("이메일", List.of(beforeEmployeeInfo.email(), employee.getEmail()));
        }
        if(!beforeEmployeeInfo.status().equals(employee.getStatus())){
            diff.put("상태", List.of(beforeEmployeeInfo.status().toString(), employee.getStatus().toString()));
        }

        changeLogService.recordLogByUpdateEmployee(employee.getEmployeeNumber(), clientIp, request.memo(), diff);

        return employeeMapper.toDto(employee);
    }

    // 직원 삭제
    @Transactional
    public void deleteEmployee(Long employeeId, String clientIp) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new NoSuchElementException("직원이 존재하지 않습니다."));

        // 직원 삭제 로그 저장
        changeLogService.recordLogByDeleteEmployee(employee.getEmployeeNumber(), clientIp, employee);

        File profile = employee.getProfile();

        if (profile != null) {
            fileRepository.deleteById(profile.getId());
            // 프로필 이미지 데이터 삭제 로직 필요
        }

        employeeRepository.deleteById(employeeId);
    }

    // 직원 수 조회
    public long getEmployeeCount(
            EmployeeStatus status,
            LocalDate fromDate,
            LocalDate toDate
    ) {
        if (fromDate == null && toDate == null) {
            fromDate = LocalDate.of(1900, 1, 1);
            toDate = LocalDate.now();
        } else if (fromDate != null && toDate == null) {
            toDate = LocalDate.now();
        } else if (fromDate == null && toDate != null) {
            fromDate = LocalDate.of(1900, 1, 1);
        }

        if (status != null) {
            return employeeRepository.countByStatusAndHireDateBetween(
                    status, fromDate, toDate
            );
        } else {
            return employeeRepository.countByHireDateBetween(
                    fromDate, toDate
            );
        }
    }

    // 직원 분포 조회
    public List<EmployeeDistributionDto> getEmployeeDistribution(
            String groupBy,
            EmployeeStatus status
    ) {
        String criteria = (groupBy == null || groupBy.isBlank())
                ? "department"
                : groupBy;

        EmployeeStatus effectiveStatus = (status == null)
                ? EmployeeStatus.ACTIVE
                : status;

        List<EmployeeRepository.EmployeeGroupCount> stats;

        switch (criteria) {
            case "department" ->
                    stats = employeeRepository.countGroupByDepartment(effectiveStatus);
            case "position" ->
                    stats = employeeRepository.countGroupByPosition(effectiveStatus);
            default ->
                    throw new IllegalArgumentException("지원하지 않는 그룹화 입니다. groupBy=" + criteria);
        }

        long total = stats.stream()
                .mapToLong(EmployeeRepository.EmployeeGroupCount::getCount)
                .sum();

        return stats.stream()
                .map(s -> new EmployeeDistributionDto(
                        s.getGroupKey(),
                        s.getCount(),
                        total > 0
                                ? (double) s.getCount() * 100 / total
                                : 0.0
                ))
                .toList();
    }

    // 직원 수 추이 조회
    @Transactional(readOnly = true)
    public List<EmployeeTrendDto> getEmployeeTrend(EmployeeTrendRequest request) {
        // from(시작 일시) : (기본값: 현재로부터 unit 기준 12개 이전)
        // to(종료 일시) : (기본값: 현재)
        // unit(시간 단위) : (day, week, month, quarter, year, 기본값: month)

        LocalDate from = request.from();
        LocalDate to = request.to() != null ? request.to() : LocalDate.now(); // 종료 일시가 없는 경우 현재 날짜로 저장

        // 시작 일시가 없는 경우 종료 일시를 기준으로 12개 이전 일시로 저장
        if (from == null) {
            switch (request.unit()) {
                case day -> from = to.minusDays(12);
                case week -> from = to.minusWeeks(12);
                case month -> from = to.minusMonths(12);
                case quarter -> from = to.minusMonths(36);
                case year -> from = to.minusYears(12);
            }
        }

        List<EmployeeTrendDto> result = new ArrayList<>();
        LocalDate targetDate = from;

        // 시작 일시가 종료 일시보다 이전인 경우 쿼리문 실행
        if (from.isBefore(to)) {
            // 종료 일시까지만 쿼리문 실행
            while (!targetDate.isAfter(to)) {
                int count = employeeRepository.findEmployeeTrend(targetDate);
                EmployeeTrendDto trend = getEmployeeTrendDto(result, targetDate, count);
                result.add(trend);

                switch (request.unit()) {
                    case day -> targetDate = targetDate.plusDays(1);
                    case week -> targetDate = targetDate.plusWeeks(1);
                    case month -> targetDate = targetDate.plusMonths(1);
                    case quarter -> targetDate = targetDate.plusMonths(3);
                    case year -> targetDate = targetDate.plusYears(1);
                }
            }
        }

        return result;
    }

    private EmployeeTrendDto getEmployeeTrendDto(
            List<EmployeeTrendDto> result,
            LocalDate targetDate,
            int count
    ) {
        // 첫 데이터의 경우 증감 비교 대상이 없으므로 0으로 저장
        if (result.isEmpty()) {
            return new EmployeeTrendDto(
                    targetDate,
                    count,
                    0,
                    0.0
            );
        }

        int beforeCount = result.get(result.size() - 1).count();
        int change = count - beforeCount;
        double changeRate = 0.0;

        // 이전 직원 수가 0명이 아닐 때만 증감률 계산
        if (beforeCount != 0) {
            changeRate = (double) change / beforeCount * 100;
            changeRate = Math.round(changeRate * 100) / 100.0;
        }

        return new EmployeeTrendDto(
                targetDate,
                count,
                change,
                changeRate
        );
    }
}
