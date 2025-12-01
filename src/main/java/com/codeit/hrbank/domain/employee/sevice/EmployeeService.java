package com.codeit.hrbank.domain.employee.sevice;

import com.codeit.hrbank.domain.department.entity.Department;
import com.codeit.hrbank.domain.department.repository.DepartmentRepository;
import com.codeit.hrbank.domain.employee.dto.EmployeeCreateRequest;
import com.codeit.hrbank.domain.employee.dto.EmployeeDto;
import com.codeit.hrbank.domain.employee.dto.EmployeeUpdateRequest;
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
import java.time.LocalDateTime;
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
        LocalDateTime hireDate = request.hireDate();
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
}
