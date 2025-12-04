package com.codeit.hrbank.department.integration.service;

import com.codeit.hrbank.domain.department.dto.*;
import com.codeit.hrbank.domain.department.entity.Department;
import com.codeit.hrbank.domain.department.projection.DepartmentWithCountEmployee;
import com.codeit.hrbank.domain.department.repository.DepartmentRepository;
import com.codeit.hrbank.domain.department.service.DepartmentService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

// SpringBootTest를 쓰고 테스트 코드를 넣고 어플리케이션을 실행 했을 떄 좀 오래 걸림
@SpringBootTest
@Transactional
public class DepartmentServiceIntegrationTest {

    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private EntityManager em;

    // 인티그레이션 테스트이고, 테스트 붙이는건 단일 테스트만 하고, @Test 안붙임. 메소드 만들기 전에 클래스 만들어서 씀
    // 큰 묶음으로 서비스 할거니까, 클래스 명을 아래와 같이 만듬
    // class CreateDapartment{} 이런식으로
    // @DisplayName => 뭐를 테스트 하는지 주석 어노테이션> 그 목적 +  테스트 실행 시 네임이 뜸. 어떤 것을 테스트하는지 볼 수 있음.
    // Nested => 내부 클래스 사용 시 해당 어노테이션 필요.
    // result 테스트 > 이름 > 어떤건지 > 메소드 위에도 @Display NAme 붙일 수 있음.
    // 붙이면 해당 displayName 으로 바뀜 > 실패가 뜨면어디서 그랬는지 바로 알 수 있음.
    // 통합테스트 안에서도 여러 개가 있고, 여러 묶음이 나올 수도 있어서 클래스로 묶는 편
    // TDD -> 테스트 주도 개발 > TDD - 테스트 코드부터 작성 > 실패 작성 > 그걸 통과하게 한다
    // 객체 지향의 진실과 오해에서 실패 코드를 작성한다는 게 예를 들어, 먼저 작성을 하고, 애초에 없는 상태로 진행하는 것
    // 예를 들면 아래와 같이 없는 user에 단일 조회를 한다고 치면, 테스트코드에서
    // 객체생성, UserDto user = userService.getUser(), assertEquals 를 서비스에서 먼저 작성하지 않고,
    // 아래 테스트 코드에 먼저 작성하는 것이다. 즉, 도메인도 없고, Dto 도 없고, userService 도 없는 상태에서 작성하고
    // 그 이후 도메인을 만들고, -> UserService 를 만들고 -> 메소드 만들고, Dto 만들고, getId 만든다.
    // ![[ 최소한 내가 원하는 기능을 다 검증할 수 있는 확실한 기능을 만들었다는 것이 TDD ]]
    // 프로젝트 실제 서비스는 1 -> 무한대.. 서비스는 계속해서 바뀜.
    // 프론트는 일일히 사실 검증하는 편임. 요구사항이 바뀌었을 때, 기능 구현 후

    // 단위 테스트 -> 순수 로직 부분만
    // 서비스 안에 레포가 있는데 순수 서비스 로직을 보고싶다-> Mock 을 씀.
    // 서비스를 when 도는 동안 레포지토리 에 접근하면 이 Mock 에 접근해줘. 하니까 실제 DB 가지 않음.
    // varify
    // 서비스 적으로만 테스트
    // 엔티티, DTO 도메인은 DB 없으니깐 순수하게 그거만 보는거
    // when -> 실행되면 뭐가 반환될지
    // 통합테스트보다 광범위하고 디테일하면서도 추상적이다.
    @DisplayName("createDepartment 부서 생성 테스트")
    @Nested
    class CreateDepartment {
        //성공 케이스
        @Test
        @DisplayName("관리 부서 생성 - 정상적인 인자값 전달 시 부서가 잘 생성")
        void createDepartment_then_result() {
            //given
            DepartmentCreateRequest request =
                    new DepartmentCreateRequest(
                            "새로운 부서",
                            "생성 테스트에서 생성한 부서",
                            LocalDate.now());

            //when
            DepartmentDto departmentDto = departmentService.createDepartment(request);

            //then
            assertEquals(request.name(), departmentDto.name());
            assertEquals(request.description(), departmentDto.description());
            assertEquals(request.establishedDate(), departmentDto.establishedDate());
        }

        //실패 케이스
        @Test
        @DisplayName("관리 부서 생성 - 중복된 부서명으로 생성 요청")
        void createDepartment_when_duplicate_name_then_illegalStateException() {
            //given
            DepartmentCreateRequest request1 =
                    new DepartmentCreateRequest("중복할 부서",
                            "생성 테스트에서 생성할 중복할 부서",
                            LocalDate.now());
            DepartmentCreateRequest request2 =
                    new DepartmentCreateRequest("중복할 부서",
                            "생성 테스트에서 중복되는 부서",
                            LocalDate.now());

            //when & then
            departmentService.createDepartment(request1);
            assertThrows(IllegalStateException.class, () -> departmentService.createDepartment(request2));
        }
    }

    @DisplayName("getDepartment 단일 조회 테스트")
    @Nested
    class GetDepartment {
        // 테스트 메소드 아예 없으면 에러남
        // 행위로 메소드 명을 지음. + 목적도 같이. 아래는 성공 시를 적음.
        // 성공 케이스
        @Test
        @DisplayName("관리 부서 단일 조회 - 호출시 값이 잘 전달")
        void getDepartment_then_result() {
            // given > 주는 거 - 어떤 자료가 필요하고 어떤 걸 넘겨줬을 때(가정)
            // 서비스의 create 함수를 쓰면 안됨 > 다른 서비스의 로직이 섞이면 안되므로 리포지토리를 가져와서 쓰는 것임
            // 실행하면 진짜 DB 에 저장됨. 그래서 보통 통합테스트라고 하는 것임. 실제의 로직과 DB 를 거치기 때문.
            // 실제 DB 까지 다녀온 saved 객체를 사용
            // 그래서 h2 를 테스트에 많이 쓰는 것임.
            Department department = new Department("개발2팀", "파트 2의 개발 2팀", LocalDate.now());
            Department saved = departmentRepository.save(department);
            // when > 언제 - 어떤 것을 실행했을 때인지(핵심) - 실행할 메서드가 이 부분에 들어감
            DepartmentDto departmentDto = departmentService.getDepartment(saved.getId());

            // then > 그 땐 - 결과를 비교를 하는 것.
            // 첫번째가 expected => 예상값. given 에서 준 거로
            // 두번쨰 actual -> 실제값. when 즉 서비스를 거쳐서 온 거로.
            // 단, 실행하면 JPA 까지 쓰니까.. 그래서 Transactional 을 걸어놔야됨. 그래야 롤백도 되고, 영속성도 먹음.
            // 1차 캐싱 때문에 헷갈릴 수 있음.
            // 메소드가 끝나면 행위가 이루어짐. persist, flush department 가 됨. @-@;;;
            assertEquals(department.getId(), departmentDto.id());
            assertEquals(department.getName(), departmentDto.name());
            assertEquals(department.getDescription(), departmentDto.description());
            assertEquals(department.getEstablishedDate(), departmentDto.establishedDate());

            //참고 : 보통 리스트 테스트는 size 를 비교하거나 첫번째, 마지막 요소를 테스팅 하기도 함.
            //메소드 끝나면 DB 정리됨. => 내가 했던 것만.
            //h2는 프론트나 포스트맨도 정리해주기 위해 쓰는 것.
        }

        // 예외 케이스
        // 실패 테스트는 우리가 예외처리 한 것을 테스트 함
        // 그래서 실제 로직을 보고 예외 파악을 하고 예외 사항을 주고 똑같이 예외가 내보내는지 테스팅
        // assert가 단증 -> 우리가 예상할 값을 줄거야.라고 단증 하는 것.
        // null 값 넣는거 그런 건 Controller 테스트에서 가능.

        // id가 아닌 부서명 조회로 단일 조회하는 경우도 service를 거쳐서 하지 말것. 번거로워도, given 에서 다시 주는 것을 추천함.
        // 참고 자료 : https://velog.io/@rainlee/DRY%EA%B0%80-%EC%95%84%EB%8B%88%EB%9D%BC-DAMP-%ED%85%8C%EC%8A%A4%ED%8A%B86
        // 참고 : 이것을 위해 Fixture 를 만들기도 한다. > 고정된 값. 여러 테스트에서 쓰는 고정된 값을 씀.
        // 처음 배우는 상황이므로 이번에는 위에 것을 쓰지 않는 방향으로 함
        @Test
        @DisplayName("관리 부서 단일 조회 - DB에 존재하지 않는 ID 로 조회 시 NoSuchElementException 예외")
        void getDepartment_when_not_found_then_throw_NoSuchElementException() {
            //given
            long id = 9999L;

            //when & then
            assertThrows(NoSuchElementException.class, () -> departmentService.getDepartment(id));
        }
    }

    @DisplayName("deleteDepartment 삭제 테스트")
    @Nested
    class DeleteDepartment {
        // 성공 케이스
        @Test
        @DisplayName("관리 부서 삭제 - 호출 시 정상적으로 부서 삭제")
        void deleteDepartment_when_success_then_result() {

            // given size 와 찾는값 있는지 없는지
            int beforeSize = departmentRepository.findAll().size();
            Department department = new Department("삭제될 부서", "삭제 할 부서입니다.", LocalDate.now());
            Department saved = departmentRepository.save(department);
            int middleSize = departmentRepository.findAll().size();

            // when
            departmentService.deleteDepartment(saved.getId());
            int afterSize = departmentRepository.findAll().size();

            // then
            assertFalse(departmentRepository.existsById(saved.getId()));
            assertEquals(beforeSize, afterSize);
        }
    }

    @DisplayName("updateDepartment 수정 테스트")
    @Nested
    class UpdateDepartment {
        // 성공 케이스
        @Test
        @DisplayName("관리 부서 수정 - 호출 시 정상적으로 부서 수정")
        void updateDapartment_when_success_then_result() {
            //given
            Department department = new Department("생성한 부서", "생성한 부서입니다.", LocalDate.now());
            Department beforeDepartment = departmentRepository.save(department);
            DepartmentUpdateRequest request = new DepartmentUpdateRequest("수정된 부서", "수정한 부서입니다.", LocalDate.now());

            //when
            DepartmentDto afterDepartment = departmentService.updateDepartment(beforeDepartment.getId(), request);

            //then
            assertEquals(beforeDepartment.getId(), afterDepartment.id());
            assertEquals(request.name(), afterDepartment.name());
            assertEquals(request.description(), afterDepartment.description());
            assertEquals(request.establishedDate(), afterDepartment.establishedDate());
        }

        // 실패 케이스
        @Test
        @DisplayName("관리 부서 수정 - id 값이 null 인 상태로 수정 요청")
        void updateDepartment_when_id_is_null_then_throw_IllegalArgumentException() {
            // given
            Long id = null;
            DepartmentUpdateRequest request = new DepartmentUpdateRequest("수정 요청명", "수정 요청할 내용입니다.", LocalDate.now());

            // when & then
            assertThrows(IllegalArgumentException.class, () -> departmentService.updateDepartment(id, request));
        }

        // 실패 케이스
        @Test
        @DisplayName("관리 부서 수정 - 없는 id 값으로 수정 요청")
        void updateDepartment_when_not_found_then_throw_IllegalArgumentException() {
            //given
            long id = 9999L;
            DepartmentUpdateRequest request = new DepartmentUpdateRequest("수정 요청명", "수정 요청할 내용입니다.", LocalDate.now());

            //when & then
            assertThrows(IllegalArgumentException.class, () -> departmentService.updateDepartment(id, request));
        }
    }

    @DisplayName("커서기반 부서 관리 리스트 조회")
    @Nested
    class GetAllDepartments {

        @Test
        @DisplayName("부서관리 - 커서기반 조회시 해당 사이즈 설정만큼 리스트 조회")
        void getDepartments_when_size_five_then_results() {
// given
            List<Department> departmentList = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                Department department = new Department("부서" + i, "부서입니다" + i, LocalDate.parse("2011-01-0" + (i % 9 + 1)));
                departmentList.add(department);
            }

            for (Department department : departmentList) {
                departmentRepository.save(department);
            }

            CursorPageRequestDepartmentDto request = new CursorPageRequestDepartmentDto(null, null, null, "ASC", "establishedDate", 5);

            // when
            CursorPageResponseDepartmentDto departmentWithCountEmployeeSlice = departmentService.getAllDepartments(request);
            int pageSize = departmentWithCountEmployeeSlice.size(); // 실제 가져온 사이즈만
            // then
            assertEquals(request.size(), pageSize);
            assertTrue(departmentWithCountEmployeeSlice.hasNext());
        }

    }
}
