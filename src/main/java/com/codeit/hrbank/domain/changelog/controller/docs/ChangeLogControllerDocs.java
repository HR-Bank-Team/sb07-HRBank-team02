package com.codeit.hrbank.domain.changelog.controller.docs;

import com.codeit.hrbank.domain.changelog.dto.ChangeLogFilter;
import com.codeit.hrbank.domain.changelog.dto.CursorPageResponseChangeLogDto;
import com.codeit.hrbank.domain.changelog.dto.DiffDto;
import com.codeit.hrbank.global.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "Change Log", description = "직원 정보 수정 이력 관리 API")
public interface ChangeLogControllerDocs {

    @Operation(summary = "직원 정보 수정 이력 목록 조회")
    @ApiResponses(
            {
                    @ApiResponse(
                            responseCode = "200",
                            description = "조회 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CursorPageResponseChangeLogDto.class),
                                    examples = @ExampleObject(
                                            name = "Change Log 목록 조회 성공 예시",
                                            value = """
                                                    {
                                                          "content": [
                                                              {
                                                                  "id": 1,
                                                                  "type": "CREATED",
                                                                  "employeeNumber": "EMP-1234-123412341-2341-1234-1234-123412341234",
                                                                  "memo": "직원 추가",
                                                                  "ipAddress": "127.0.0.1",
                                                                  "at": "2025-11-15 16:00:00.000"
                                                              }
                                                          ],
                                                          "nextCursor": "2025-11-15 16:00:00.000",
                                                          "nextIdAfter": 1,
                                                          "size": 1,
                                                          "totalElements": 1,
                                                          "hasNext": false
                                                      }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "sortDirection, sortAt, size는 반드시 있어야 합니다.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    ResponseEntity<CursorPageResponseChangeLogDto> getChangeLogs(ChangeLogFilter changeLogFilter);

    @Operation(summary = "직원 정보 수정 이력 상세 조회")
    @ApiResponses(
            {
                    @ApiResponse(
                            responseCode = "200",
                            description = "상세 조회 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DiffDto.class),
                                    examples = @ExampleObject(
                                            name = "상세 목록 조회 성공 예시",
                                            value = """
                                                    [
                                                        {
                                                            "propertyName":"직함",
                                                            "before":"사원",
                                                            "after":"대리"
                                                        }
                                                    ]
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "이력을 찾을 수 없음",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    ResponseEntity<List<DiffDto>> getChangeLogDetails(Long id);

    @Operation(summary = "기간 별 수정 이력 건수 조회")
    @ApiResponses(
            {
                    @ApiResponse(
                            responseCode = "200",
                            description = "건수 조회 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = "11"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "잘못된 요청 또는 유효하지 않은 날짜 범위",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    ResponseEntity<Long> getLogCount(LocalDateTime fromDate, LocalDateTime toDate);
}
