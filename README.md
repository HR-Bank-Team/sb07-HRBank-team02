# 🏺 뒤주 (HR Bank)

> 곡식을 담는 ‘뒤주’에서 착안하여, 소중한 인적자원을 안전하고 체계적으로 관리하는 HR 시스템
> 

🔗 **배포 URL**

https://sb07-hrbank-team02-hr-bank.up.railway.app

---

## 📌 프로젝트 소개

**HR Bank**는 기업의 인사 정보를 편리하게 관리하는 HR 시스템입니다.

### ✨ 주요 특징

- 부서 및 직원 정보 **CRUD**
- **부서명 / 이메일 중복 검증**
- 프로필 이미지 **파일 업로드 & 조회**
- 데이터 변경 감지 및 **자동 백업 (1시간 주기)**
- 수정 이력 **로그 추적**
- 검색, 정렬, **커서 기반 페이지네이션**
- 대시보드 **통계 시각화**
    - 직원 수 추이
    - 부서/직무 분포
    - 최근 변화 분석

---

## 📆 프로젝트 기간

> 2025.11.27 ~ 2025.12.08
> 

---

## 🛠 기술 스택

| 분야 | 기술 |
| --- | --- |
| Backend | Spring Boot, Spring Security, Spring Data JPA, Spring Validation, springdoc-openapi, OpenCSV |
| Database | PostgreSQL |
| Common | Git & GitHub, Discord, Notion |
| Deployment | railway.io |

---
## 👥 팀원 소개

| 이름 | 역할 | GitHub |
| --- | --- | --- |
| 김태언 | 팀장/부서관리 | https://github.com/Taeeon-kim |
| 최지혜 | 부서관리 | https://github.com/ChoiJiHye950 |
| 조성만 | 직원관리 | https://github.com/BetterCodings |
| 최태훈 | 직원관리 | https://github.com/Tae705 |
| 황준영 | 백업/파일관리 | https://github.com/OfficialHwempire |
| 안대식 | 수정 이력관리 | https://github.com/ian-i-an |

---

## 📌 팀원별 구현 기능

### 🔷 부서관리

- **김태언**
- **최지혜**

### 🔶 직원관리

- **조성만**
- **최태훈**
  - 직원 분포도(GroupBy) 조회 기능 구현
    부서·직무 단위로 직원 수를 집계하는 통계 API 개발
    @Query 기반 커스텀 쿼리 작성
  - 직원 수 조회 API 구현 (Count API)
    상태(status), 기간(from/to) 조건에 따라 직원 수를 집계하는 API 개발
    비즈니스 규칙에 따라 기본 날짜 범위 적용 & 예외 처리 강화
    Swagger 문서화 및 응답 DTO 구조 개선
### 📁 백업관리 & 파일관리

- **황준영**

### 📝 수정이력관리

- **안대식**

---

## 🧩 트러블슈팅

### 🔷 부서관리

*(작성 예정)*

### 🔶 직원관리
- **시간 파라미터 타입 불일치 문제**
  - **문제:** 프론트에서 전달한 시간 정보를 백엔드에서 정상적으로 수신하지 못하는 문제  
  - **발생 원인:** 시간 타입 파라미터가 프론트엔드에서 전달하는 형식과 불일치  
  - **해결:** 파라미터 타입을 프론트에서 요구하는 시간 형식으로 변경하여 호환성 확보  
  - **배운 점:** 시간 타입은 여러 종류가 있어 요청 포맷에 맞는 적절한 타입 선택이 중요함  


### 📁 백업관리 & 파일관리

*(작성 예정)*

### 📝 수정이력관리

*(작성 예정)*

---

## 📂 파일 구조

```
sb07-HRBank-team02
├─ src
│  ├─ main
│  │  ├─ java
│  │  │  └─ com
│  │  │     └─ codeit
│  │  │        └─ hrbank
│  │  │           ├─ domain
│  │  │           │  ├─ backup
│  │  │           │  │  ├─ controller
│  │  │           │  │  │  ├─ BackupController.java
│  │  │           │  │  │  └─ docs
│  │  │           │  │  │     └─ BackupControllerDocs.java
│  │  │           │  │  │
│  │  │           │  │  ├─ dto
│  │  │           │  │  │  ├─ export
│  │  │           │  │  │  │  └─ ExportEmployeeDto.java
│  │  │           │  │  │  ├─ request
│  │  │           │  │  │  │  └─ CursorBackupRequestDto.java
│  │  │           │  │  │  └─ response
│  │  │           │  │  │     ├─ BackupDto.java
│  │  │           │  │  │     └─ CursorPageResponseBackupDto.java
│  │  │           │  │  │
│  │  │           │  │  ├─ entity
│  │  │           │  │  │  ├─ Backup.java
│  │  │           │  │  │  └─ BackupEnum
│  │  │           │  │  │     ├─ BackupSortDirection.java
│  │  │           │  │  │     ├─ BackupSortDirectionConverter.java
│  │  │           │  │  │     ├─ BackupSortField.java
│  │  │           │  │  │     ├─ BackupSortFieldConverter.java
│  │  │           │  │  │     └─ BackupStatus.java
│  │  │           │  │  │
│  │  │           │  │  ├─ mapper
│  │  │           │  │  │  ├─ BackupMapper.java
│  │  │           │  │  │  ├─ CursorPageBackupMapper.java
│  │  │           │  │  │  └─ ExportEmployeeMapper.java
│  │  │           │  │  │
│  │  │           │  │  ├─ repository
│  │  │           │  │  │  ├─ BackSliceRepository.java
│  │  │           │  │  │  └─ BackupRepository.java
│  │  │           │  │  │
│  │  │           │  │  └─ sevice
│  │  │           │  │     ├─ BackupRegister.java
│  │  │           │  │     ├─ BackupScheduler.java
│  │  │           │  │     ├─ BackupService.java
│  │  │           │  │     └─ IBackupService.java
│  │  │           │  │
│  │  │           │  ├─ base
│  │  │           │  │  ├─ BaseEntity.java
│  │  │           │  │  └─ BaseUpdatableEntity.java
│  │  │           │  │
│  │  │           │  ├─ changelog
│  │  │           │  │  ├─ controller
│  │  │           │  │  │  ├─ ChangeLogController.java
│  │  │           │  │  │  └─ docs
│  │  │           │  │  │     └─ ChangeLogControllerDocs.java
│  │  │           │  │  │
│  │  │           │  │  ├─ dto
│  │  │           │  │  │  ├─ ChangeLogDto.java
│  │  │           │  │  │  ├─ ChangeLogFilter.java
│  │  │           │  │  │  ├─ CreateLogDetailCommand.java
│  │  │           │  │  │  ├─ CursorPageResponseChangeLogDto.java
│  │  │           │  │  │  ├─ DeleteLogDetailCommand.java
│  │  │           │  │  │  ├─ DiffCommand.java
│  │  │           │  │  │  └─ DiffDto.java
│  │  │           │  │  │
│  │  │           │  │  ├─ entity
│  │  │           │  │  │  ├─ ChangeLog.java
│  │  │           │  │  │  ├─ ChangeLogType.java
│  │  │           │  │  │  └─ Diff.java
│  │  │           │  │  │
│  │  │           │  │  ├─ mapper
│  │  │           │  │  │  ├─ ChangeLogMapper.java
│  │  │           │  │  │  └─ DiffMapper.java
│  │  │           │  │  │
│  │  │           │  │  ├─ repository
│  │  │           │  │  │  ├─ ChangeLogCustomRepository.java
│  │  │           │  │  │  ├─ ChangeLogCustomRepositoryImpl.java
│  │  │           │  │  │  ├─ ChangeLogRepository.java
│  │  │           │  │  │  └─ DiffRepository.java
│  │  │           │  │  │
│  │  │           │  │  └─ service
│  │  │           │  │     └─ ChangeLogService.java
│  │  │           │  │
│  │  │           │  ├─ department
│  │  │           │  │  ├─ controller
│  │  │           │  │  │  ├─ DepartmentController.java
│  │  │           │  │  │  └─ docs
│  │  │           │  │  │     └─ DepartControllerDocs.java
│  │  │           │  │  │
│  │  │           │  │  ├─ dto
│  │  │           │  │  │  ├─ CursorPageRequestDepartmentDto.java
│  │  │           │  │  │  ├─ CursorPageResponseDepartmentDto.java
│  │  │           │  │  │  ├─ DepartmentCreateRequest.java
│  │  │           │  │  │  ├─ DepartmentDto.java
│  │  │           │  │  │  └─ DepartmentUpdateRequest.java
│  │  │           │  │  │
│  │  │           │  │  ├─ entity
│  │  │           │  │  │  └─ Department.java
│  │  │           │  │  │
│  │  │           │  │  ├─ mapper
│  │  │           │  │  │  └─ DepartmentMapper.java
│  │  │           │  │  │
│  │  │           │  │  ├─ projection
│  │  │           │  │  │  └─ DepartmentWithCountEmployee.java
│  │  │           │  │  │
│  │  │           │  │  ├─ repository
│  │  │           │  │  │  └─ DepartmentRepository.java
│  │  │           │  │  │
│  │  │           │  │  └─ service
│  │  │           │  │     └─ DepartmentService.java
│  │  │           │  │
│  │  │           │  ├─ employee
│  │  │           │  │  ├─ controller
│  │  │           │  │  │  ├─ EmployeeController.java
│  │  │           │  │  │  └─ docs
│  │  │           │  │  │     └─ EmployeeControllerDocs.java
│  │  │           │  │  │
│  │  │           │  │  ├─ dto
│  │  │           │  │  │  ├─ CursorPageRequestEmployeeDto.java
│  │  │           │  │  │  ├─ CursorPageResponseEmployeeDto.java
│  │  │           │  │  │  ├─ EmployeeCreateRequest.java
│  │  │           │  │  │  ├─ EmployeeDistributionDto.java
│  │  │           │  │  │  ├─ EmployeeDto.java
│  │  │           │  │  │  ├─ EmployeeTrendDto.java
│  │  │           │  │  │  ├─ EmployeeTrendRequest.java
│  │  │           │  │  │  ├─ EmployeeUpdateRequest.java
│  │  │           │  │  │  ├─ SortDirection.java
│  │  │           │  │  │  ├─ SortField.java
│  │  │           │  │  │  └─ TimeUnit.java
│  │  │           │  │  │
│  │  │           │  │  ├─ entity
│  │  │           │  │  │  ├─ Employee.java
│  │  │           │  │  │  └─ EmployeeStatus.java
│  │  │           │  │  │
│  │  │           │  │  ├─ mapper
│  │  │           │  │  │  └─ EmployeeMapper.java
│  │  │           │  │  │
│  │  │           │  │  ├─ repository
│  │  │           │  │  │  └─ EmployeeRepository.java
│  │  │           │  │  │
│  │  │           │  │  └─ service
│  │  │           │  │     └─ EmployeeService.java
│  │  │           │  │
│  │  │           │  └─ file
│  │  │           │     ├─ controller
│  │  │           │     │  └─ FileController.java
│  │  │           │     │
│  │  │           │     ├─ entity
│  │  │           │     │  └─ File.java
│  │  │           │     │
│  │  │           │     ├─ repository
│  │  │           │     │  └─ FileRepository.java
│  │  │           │     │
│  │  │           │     ├─ service
│  │  │           │     │  ├─ FileService.java
│  │  │           │     │  └─ FileStorage.java
│  │  │           │     │
│  │  │           │     └─ util
│  │  │           │        └─ CsvUtil.java
│  │  │           ├─ global
│  │  │           │  ├─ config
│  │  │           │  │  └─ SchedulerConfig.java
│  │  │           │  │
│  │  │           │  ├─ exception
│  │  │           │  │  ├─ ErrorResponse.java
│  │  │           │  │  └─ ExceptionControllerAdvice.java
│  │  │           │  │
│  │  │           │  └─ util
│  │  │           └─  HrBankApplication.java
│  │  │            
│  │  └─ resources
│  │     ├─ application.yml
│  │     ├─ schema.sql
│  │     └─ static/
│  │
│  └─ test
│     └─ java
│        └─ com
│           └─ codeit
│              └─ hrbank
│                 ├─ HrBankApplicationTests.java
│                 ├─ backup/
│                 ├─ changelog/
│                 ├─ department/
│                 ├─ employee/
│                 └─ file/
│
├─ .gitignore
├─ build.gradle
├─ gradlew
├─ gradlew.bat
├─ settings.gradle
└─ storage
   ├─ backup/
   ├─ log/
   └─ profile/

```

---

## 📊 클래스 다이어그램

*(추가 예정)*

### 🔶 직원관리
<img width="888" height="653" alt="image" src="https://github.com/user-attachments/assets/e990f07a-7f6c-42d9-b1da-cec4f54c333a" />


## 🔄 시퀀스 다이어그램

### 🔷 부서관리

### 🔶 직원관리
<img width="692" height="611" alt="image" src="https://github.com/user-attachments/assets/2b620892-403a-4183-b669-c29e25315660" />

### 📁 백업관리 & 파일관리

### 📝 수정이력관리

<img width="372" height="594" alt="sequence_diagram" src="https://github.com/user-attachments/assets/4e3901e2-cdda-4d86-9e3a-036559b09d1b" />

---

## 📎 팀 협업 문서

📰 Notion 문서

https://www.notion.so/HR-Bank-2b8dc875efeb80459584cd76aff6af9f

---

## 📝 프로젝트 회고록

### 📌 팀 발표 자료

- *(추후 업로드)*

### 👤 개인 회고

| 이름 | 링크 |
| --- | --- |
| 김태언 | (추후 추가) |
| 최지혜 | https://www.notion.so/2c2dc875efeb800fa2a6dc6ad56b7f16 |
| 조성만 | https://smjoe0302.tistory.com/18 |
| 최태훈 | https://www.notion.so/HR-Bank-_-2bf844450e228043a132c2cd49313db6 |
| 황준영 | https://www.notion.so/HR-Bank-2c2ebb626db68009920ef4f6f287d0fe |
| 안대식 | https://ian1290.tistory.com/37 |
