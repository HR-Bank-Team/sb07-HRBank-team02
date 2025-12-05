package com.codeit.hrbank.domain.department.controller.docs;

import com.codeit.hrbank.domain.department.dto.*;
import com.codeit.hrbank.global.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Department", description = "Department API")
public interface DepartControllerDocs {

    @Operation(summary = "전체 Department 목록 조회")
    @ApiResponses(
            {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Department 목록 조회 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CursorPageResponseDepartmentDto.class),
                                    examples = @ExampleObject(
                                            name = "Department 목록 조회 성공 예시",
                                            value = """
                                                    {
                                                          "content": [
                                                              {
                                                                  "id": 16,
                                                                  "name": "마케팅 부서",
                                                                  "description": "마케팅 부서 설명입니다.",
                                                                  "establishedDate": "2021-06-15",
                                                                  "employeeCount": 999
                                                              },
                                                              {
                                                                  "id": 34,
                                                                  "name": "마크 부서",
                                                                  "description": "마크 부서 설명입니다.",
                                                                  "establishedDate": "2011-01-11",
                                                                  "employeeCount": 999
                                                              }
                                                          ],
                                                          "nextCursor": "소고기 부서",
                                                          "nextIdAfter": 37,
                                                          "size": 2,
                                                          "totalElements": 5,
                                                          "hasNext": true
                                                      }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "정렬 기준은 null, name, establishedDate 중 하나여야 합니다.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    ResponseEntity<CursorPageResponseDepartmentDto> getAllDepartments(CursorPageRequestDepartmentDto request);


    @Operation(summary = "Department 상세 조회")
    @ApiResponses(
            {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Department 조회 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DepartmentDto.class),
                                    examples = @ExampleObject(
                                            name = "Department 목록 조회 성공 예시",
                                            value = """
                                                    {
                                                            "id": 13,
                                                            "name": "회계 부서",
                                                            "description": "회계 부서 설명입니다.",
                                                            "establishedDate": "2020-01-01",
                                                            "employeeCount": 999
                                                        }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "해당 ID 를 가진 부서가 없습니다.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    ResponseEntity<DepartmentDto> getDepartment(Long id);


    @Operation(summary = "Department 부서 생성")
    @ApiResponses(
            {
                    @ApiResponse(
                            responseCode = "201",
                            description = "부서 생성 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DepartmentDto.class),
                                    examples = @ExampleObject(
                                            name = "부서 성공 예시",
                                            value = """
                                                    {
                                                        "id": 39,
                                                        "name": "윈터 부서",
                                                        "description": "겨울 부서입니다.",
                                                        "establishedDate": "2022-11-11",
                                                        "employeeCount": 999
                                                    }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "중복된 부서명입니다.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }

    )
    ResponseEntity<DepartmentDto> createDepartment(DepartmentCreateRequest request);

    @Operation(summary = "Department 부서 삭제")
    @ApiResponses(
            {
                    @ApiResponse(
                            responseCode = "204",
                            description = "부서 삭제 성공",
                            content = @Content(
                                    mediaType = "text/plain"
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "잘못된 요청 또는 지원하지 않는 정렬 필드",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }

    )
    ResponseEntity<Void> deleteDepartment(Long id);

    @Operation(summary = "Department 부서 수정")
    @ApiResponses(
            {
                    @ApiResponse(
                            responseCode = "200",
                            description = "부서 수정 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DepartmentDto.class),
                                    examples = @ExampleObject(
                                            name = "부서 성공 예시",
                                            value = """
                                                    {
                                                        "id": 13,
                                                        "name": "Add your name in the body",
                                                        "description": "ddd",
                                                        "establishedDate": "2013-01-01",
                                                        "employeeCount": 999
                                                    }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "잘못된 요청 또는 지원하지 않는 정렬 필드",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }

    )
    ResponseEntity<DepartmentDto> updateDepartment(Long id, DepartmentUpdateRequest request);
}
