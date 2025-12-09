# 🏺 뒤주 (HR Bank)

> 곡식을 담는 ‘뒤주’에서 착안하여, 소중한 인적자원을 안전하고 체계적으로 관리하는 HR 시스템
> 

🔗 **배포 URL**

https://sb07-hrbank-team02-hr-bank.up.railway.app

---

## 📌 프로젝트 소개

**뒤주**는 기업의 인사 정보를 편리하게 관리하는 HR 시스템입니다.

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
  - 부서 삭제, 수정 서비스, 레포지토리 레이어 구현
  - 부서 요청 응답 DTO, Mapper 구현 및 개선
  - 부서 목록 조회 JPQL 쿼리 조회 성능 최적화
  - 부서 목록 조회 통합테스트 작성
  - 컨트롤러 Swagger 문서화 
- **최지혜**
  - 부서 생성, 목록 조회 기능 구현
  - 부서 생성, 단일 조회, 삭제, 수정 서비스 통합 테스트 작성 
  - 같은 도메인을 맡은 팀원과 함께 swagger 작성

### 🔶 직원관리

- **조성만**
  - 서비스 레이어 로직 구현
    - 직원 등록/수정/삭제
    - 직원 상세 조회
  - 서비스 + 레포지토리(JPQL 쿼리) 구현
    - 직원 목록 조회 (조건 검색, 커서 페이지네이션 적용) 
  - 컨트롤러 + 서비스 + 레포지토리 통합 구현
    - 직원 수 추이 조회
  - 직원 서비스 기능 통합 테스트 작성
    
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
- 주요 기여 내용:
    - 파일 메타 정보 관리
    - 파일 데이터 로컬 디스크 저장
    - 데이터 백업 관리
    - 데이터 백업 배치 시스템
  
- 구현한 기능 목록:
    - File 엔티티 서비스 , 컨트롤러, 레포지토리 레이어 구현
    - 로컬 디스크 에 파일 저장 로직 구현  (FileStorage)
    - 데이터 백업 서비스, 컨트롤러, 레포지토리 레이어 구현
    - 데이터 백업 주기적으로 진행하는 배치 시스템 구현

### 📝 수정이력관리

- **안대식**
  - 직원 변경 사항을 추적하기 위한 **수정 이력(Change Log) 및 상세 변경 내역(Diff) 생성 로직 구현**
  - 수정 이력 **건수 조회 API** 개발을 통한 통계 기능 지원
  - 사번, 내용, 기간, 유형, IP 등 **복합 조건 기반 검색 기능 구현**
  - 시간순 / IP순 **정렬 기능**과 함께 **커서 기반 페이지네이션** 적용
---

## 🧩 트러블슈팅

### 🔷 부서관리
- **검색 & 페이지네이션 오류 및 테스트 기반 개선**
  - **문제:** 검색·페이지네이션 로직이 특정 조건에서 올바르게 동작하지 않고 기존 기능이 반복적으로 깨짐
  - **발생 원인:** 테스트 코드 부재로 다양한 입력 조합과 리그레션 케이스가 검증되지 못함
  - **해결:** 문제 쿼리 수정 후 테스트 코드를 작성해 리그레션(= 프로그램 변경 후 이전에 고쳤던 버그가 다시 발생하거나 새 버그가 생기는 현상(회귀 버그)을 확인하는 회귀 테스트,Regression Test 를 의미)을 자동으로 방지하도록 개선
  - **느낀점:** 테스트 기반 개발의 중요성을 체감했고 이후 기능 개발부터 테스트를 고려해야 한다는 교훈을 얻음

- **부서별 직원수 조회 성능 최적화**
  - **문제:** 부서 목록 조회 시, 각 부서별 직원 수를 별도로 조회해 조회 속도가 급격히 느려짐
  - **발생 원인:** 부서 수만큼 직원 테이블을 반복 조회하며 COUNT 수행 → N+1 구조 + Full Scan 반복
  - **해결:** JOIN + GROUP BY + COUNT로 한 번의 쿼리(단일 쿼리)로 조회, 불필요한 필드는 프로젝션 적용으로 부서 50만/직원 10만 기준 50분 이상 걸리던 조회를 600ms로 단축(수천배 개선)
  - **느낀점:** 단순 LAZY 문제가 아니라, 쿼리 설계와 호출 방식 자체가 N+1을 만든다는 것을 깨달음

### 🔶 직원관리
- **시간 파라미터 타입 불일치 문제**
  - **문제:** 프론트에서 전달한 시간 정보를 백엔드에서 정상적으로 수신하지 못하는 문제  
  - **발생 원인:** 시간 타입 파라미터가 프론트엔드에서 전달하는 형식과 불일치  
  - **해결:** 파라미터 타입을 프론트에서 요구하는 시간 형식으로 변경하여 호환성 확보  
  - **배운 점:** 시간 타입은 여러 종류가 있어 요청 포맷에 맞는 적절한 타입 선택이 중요함  

### 🔶&📁 직원관리 & 백업 관리
- **배포 환경에서 실제 클라이언트 IP 저장 문제**
  - **문제:** 로컬 환경과 달리 배포 환경에서는 클라이언트의 실제 IP가 아닌 프록시 IP를 저장하는 문제 발생"
  - **발생 원인:** **HTTP Servlet Request의 getRemoteAddr()이 프록시 IP를 저장
  - **해결:** HTTP Servlet Request의 X-Forwarded-For 헤더에서 실제 클라이언트 IP를 추출하여 저장
  - **배운 점:** 동일한 코드가 로컬 환경에서 잘 동작하더라도 배포 환경에서는 다를 수 있음을 배웠다.

### 📁 백업관리 & 파일관리

- **트랜잭션 중간 상태 반영 불가 문제**
    - **문제:** 백업 생성 로직이 하나의 트랜잭션 내에서 처리되어, 진행중 → 완료됨 상태를 중간에 DB에 반영 불가
    - **발생 원인:** 트랜잭션이 완료되기 전까지는 변경 내용이 영속성 컨텍스트에만 존재하고 DB에 반영 불가
    - **해결:** 트랜잭션 구조와 서비스 흐름을 분석한 후, 요구사항에 맞지 않는 부분을 팀과 협의하여 현실적인 상태 로직으로 재설계
    - **배운 점:** 명세 요구를 그대로 따르기보다 기술적 제약과 서비스 흐름을 모두 고려해 팀과 소통하며 최적의 구현 방식을 찾는 것이 중요함

### 📝 수정이력관리
- **동적 검색 조건 처리 방식 개선**
    - **문제:** 사번, 기간, 유형, IP 등 선택적으로 적용되는 다양한 검색 조건을 JPQL만으로 처리하기 어려움
    - **발생 원인:** 문자열 기반 JPQL은 조건이 추가될수록 분기 처리가 복잡해지고 가독성 및 유지보수성이 급격히 저하됨
    - **해결:** 수동으로 동적 쿼리를 구성하여 조건별 쿼리 생성 로직을 분리하고, 복합 조건을 유연하게 조합할 수 있는 구조로 개선함
    - **배운 점:** 복잡한 동적 검색 환경에서는 타입 안정성과 조합 가능성을 고려한 쿼리 설계가 중요하며, Querydsl의 필요성과 구조적 장점을 이해함


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

## 🔄 시퀀스 다이어그램

### 🔷 부서관리
<img width="1115" height="813" alt="departmentSequence" src="https://github.com/user-attachments/assets/6bfdeb4a-7dba-49d2-b462-a147db6c35c6" />

### 🔶 직원관리
<img width="1095" height="810" alt="직원 관리 시퀀스 다이어그램" src="https://github.com/user-attachments/assets/73c5682f-141c-4ef9-8fb3-f42db6c2c549" />



### 📁 백업관리 & 파일관리
<img width="1877" height="1305" alt="백업 시퀀스 다이어그램" src="https://github.com/user-attachments/assets/b5f199c6-83c4-4266-a00c-321a6ba6dbbe" />

### 📝 수정이력관리

<img width="372" height="594" alt="sequence_diagram" src="https://github.com/user-attachments/assets/4e3901e2-cdda-4d86-9e3a-036559b09d1b" />

---

## 📎 팀 협업 문서

📰 Notion 문서

https://www.notion.so/HR-Bank-2b8dc875efeb80459584cd76aff6af9f

---

## 📝 프로젝트 회고록

### 📌 팀 발표 영상 및 자료
[2팀_HR BANK_발표영상](https://www.youtube.com/watch?v=HIN6LyQ1zBQ)  
[2팀_HR Bank_발표자료.pdf](https://github.com/user-attachments/files/24033263/2._HR.Bank_.pdf)


### 👤 개인 회고

| 이름 | 링크 |
| --- | --- |
| 김태언 | https://innovative-sunshine-4ce.notion.site/2c086ebf7b7f80d89ae7f7549bf51d65 |
| 최지혜 | https://www.notion.so/2c2dc875efeb800fa2a6dc6ad56b7f16 |
| 조성만 | https://smjoe0302.tistory.com/18 |
| 최태훈 | https://www.notion.so/HR-Bank-_-2bf844450e228043a132c2cd49313db6 |
| 황준영 | https://officialhwempire.github.io/posts/SprintProject1/ |
| 안대식 | https://ian1290.tistory.com/37 |
