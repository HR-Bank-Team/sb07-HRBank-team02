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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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

    // 직원 등록
    // 메모 관련 수정 이력 로직 추가 필요
    @Transactional
    public EmployeeDto createEmployee(EmployeeCreateRequest request, MultipartFile file) {
        // 이메일 중복 검증
        if(employeeRepository.existsByEmail(request.email())){
            throw new IllegalArgumentException("이미 존재하는 이메일 입니다.");
        }

        // 부서 정보 불러오기
        Department department = departmentRepository.findById(request.departmentId())
                .orElseThrow(() -> new NoSuchElementException("부서가 존재하지 않습니다."));

        // 사번 정보 생성
        int year = LocalDate.now().getYear();
        String employeeNumber = String.format("EMP-%d-%s", year, UUID.randomUUID());

        File profile = null;

        // 프로필 이미지 정보 생성
        // 파일 request DTO 생성시 변경 예정
        if(file != null) {
            profile = new File(
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getSize()
            );

            // fileStorage 저장 코드 추가 필요
        }

        Employee newEmployee = new Employee(
                request.name(),
                request.position(),
                request.email(),
                request.hireDate(),
                employeeNumber,
                EmployeeStatus.ACTIVE, // 재직중 상태로 초기화
                profile,
                department
        );

        fileRepository.save(profile);
        employeeRepository.save(newEmployee);
        return employeeMapper.toDto(newEmployee);
    }

    // 직원 상세 조회
    public EmployeeDto getEmployee(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new NoSuchElementException("직원이 존재하지 않습니다."));
        return employeeMapper.toDto(employee);
    }

    // 직원 목록 조회
    public List<EmployeeDto> getAllEmployee() {
        List<Employee> employees = employeeRepository.findAll();
        return employeeMapper.toDto(employees);
    }

    // 직원 목록 수정
    // 메모 관련 수정 이력 로직 추가 필요
    @Transactional
    public EmployeeDto updateEmployee(Long EmployeeId, EmployeeUpdateRequest request, MultipartFile file) {
        String name = request.name();
        String position = request.position();
        String email = request.email();
        LocalDate hireDate = request.hireDate();
        EmployeeStatus employeeStatus = request.status();

        // 이메일 검증 로직 필요한가?

        Employee employee = employeeRepository.findById(EmployeeId)
                .orElseThrow(() -> new NoSuchElementException("직원이 존재하지 않습니다."));

        Department department = departmentRepository.findById(request.departmentId())
                .orElseThrow(() -> new NoSuchElementException("부서가 존재하지 않습니다."));

        File profile = null;

        // 프로필 이미지 정보 생성
        if(file != null) {
            profile = new File(
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getSize()
            );

            // 기존 프로필 이미지 삭제 코드 필요
            // 새로운 프로필 이미지 저장 코드 필요

            fileRepository.save(profile); // 새로운 프로필 이미지 정보 저장
        }

        employee.update(name, position, email, hireDate, employeeStatus, profile, department);
        employeeRepository.save(employee); // 수정된 직원 정보 저장

        return employeeMapper.toDto(employee);
    }

    @Transactional
    public void deleteEmployee(Long EmployeeId) {
        Employee employee = employeeRepository.findById(EmployeeId)
                .orElseThrow(() -> new NoSuchElementException("직원이 존재하지 않습니다."));

        File profile = employee.getProfile();

        // 프로필 이미지 삭제(있는 경우)
        if(profile != null) {
            fileRepository.deleteById(profile.getId());
        }

        employeeRepository.deleteById(EmployeeId);
    }

    @Transactional
    public List<EmployeeTrendDto> getEmployeeTrend(EmployeeTrendRequest request) {
        // from(시작 일시) : (기본값: 현재로부터 unit 기준 12개 이전)
        // to(종료 일시) : (기본값: 현재)
        // unit(시간 단위) : (day, week, month, quarter, year, 기본값: month)


        LocalDate from = request.from();
        LocalDate to = request.to() != null ? request.to() : LocalDate.now(); // 종료 일시가 없는 경우 현재 날짜로 저장

        // 시작 일시가 없는 경우 종료 일시를 기준으로 12개 이전 일시로 저장
        if(from == null){
            switch(request.unit()){
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
        if(from.isBefore(to)){
            // 종료 일시까지만 쿼리문 실행
            while(!targetDate.isAfter(to)){
                int count = employeeRepository.findEmployeeTrend(targetDate);

                EmployeeTrendDto employeeTrend = getEmployeeTrendDto(result, targetDate, count);

                result.add(employeeTrend);

                switch(request.unit()){
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

    private EmployeeTrendDto getEmployeeTrendDto(List<EmployeeTrendDto> result, LocalDate targetDate, int count) {
        EmployeeTrendDto employeeTrend;

        // 첫 데이터의 경우 증감 비교 대상이 없으므로 0으로 저장
        if(result.isEmpty()) {
            employeeTrend = new EmployeeTrendDto(
                    targetDate,
                    count,
                    0,
                    0.0
                    );
        }
        else {
            int beforeCount = result.get(result.size() - 1).count();
            int change = count - beforeCount;
            double changeRate = 0.0;

            // 이전 직원 수가 0명이 아닐 때만 증감률 계산
            if(beforeCount != 0) {
                changeRate = (double) change / beforeCount * 100;
                changeRate = Math.round(changeRate * 100) / 100.0;
            }
            employeeTrend = new EmployeeTrendDto(
                    targetDate,
                    count,
                    change,
                    changeRate
                    );
        }
        return employeeTrend;
    }
}
