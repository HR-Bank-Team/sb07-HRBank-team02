package com.codeit.hrbank.domain.employee.service;

import com.codeit.hrbank.domain.department.entity.Department;
import com.codeit.hrbank.domain.department.repository.DepartmentRepository;
import com.codeit.hrbank.domain.employee.dto.*;
import com.codeit.hrbank.domain.employee.entity.Employee;
import com.codeit.hrbank.domain.employee.entity.EmployeeStatus;
import com.codeit.hrbank.domain.employee.mapper.EmployeeMapper;
import com.codeit.hrbank.domain.employee.repository.EmployeeRepository;
import com.codeit.hrbank.domain.file.entity.File;
import com.codeit.hrbank.domain.file.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final FileRepository fileRepository;

    private final EmployeeMapper employeeMapper;

    // ===========================
    // 직원 등록
    // ===========================
    @Transactional
    public EmployeeDto createEmployee(EmployeeCreateRequest request, MultipartFile file) {
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

        employeeRepository.save(newEmployee);
        return employeeMapper.toDto(newEmployee);
    }

    // ===========================
    // 직원 상세 조회
    // ===========================
    @Transactional(readOnly = true)
    public EmployeeDto getEmployee(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new NoSuchElementException("직원이 존재하지 않습니다."));

        return employeeMapper.toDto(employee);
    }

    // ===========================
    // 직원 전체 조회
    // ===========================
    @Transactional(readOnly = true)
    public List<EmployeeDto> getAllEmployee() {
        List<Employee> employees = employeeRepository.findAll();
        return employeeMapper.toDto(employees);
    }

    // ===========================
    // 직원 정보 수정
    // ===========================
    @Transactional
    public EmployeeDto updateEmployee(Long employeeId, EmployeeUpdateRequest request, MultipartFile file) {

        if (employeeRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("이미 존재하는 이메일 입니다.");
        }

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new NoSuchElementException("직원이 존재하지 않습니다."));

        Department department = departmentRepository.findById(request.departmentId())
                .orElseThrow(() -> new NoSuchElementException("부서가 존재하지 않습니다."));

        File profile = null;

        if (file != null) {
            profile = new File(
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getSize()
            );

            fileRepository.save(profile);
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
        return employeeMapper.toDto(employee);
    }

    // ===========================
    // 직원 삭제
    // ===========================
    @Transactional
    public void deleteEmployee(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new NoSuchElementException("직원이 존재하지 않습니다."));

        File profile = employee.getProfile();

        if (profile != null) {
            fileRepository.deleteById(profile.getId());
        }

        employeeRepository.deleteById(employeeId);
    }

    // =====================================================================
    // 직원 "수" 조회 (LocalDate 적용)
    // =====================================================================
    public EmployeeCountDto getEmployeeCount(
            EmployeeStatus status,
            LocalDate fromDate,
            LocalDate toDate
    ) {
        long count = employeeRepository.countByStatusAndHireDateBetween(
                status,
                fromDate,
                toDate
        );
        return new EmployeeCountDto(count);
    }

    // =====================================================================
    // 직원 "분포" 조회
    // =====================================================================
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

    // =====================================================================
    // 직원 "증감 추이" 조회 (Trend)
    // =====================================================================
    @Transactional(readOnly = true)
    public List<EmployeeTrendDto> getEmployeeTrend(EmployeeTrendRequest request) {

        LocalDate from = request.from();
        LocalDate to = request.to() != null ? request.to() : LocalDate.now();

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

        if (from.isBefore(to)) {
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
