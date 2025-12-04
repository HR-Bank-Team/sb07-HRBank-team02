package com.codeit.hrbank.domain.backup.controller.docs;

import com.codeit.hrbank.domain.backup.dto.response.BackupDto;
import com.codeit.hrbank.domain.backup.dto.response.CursorPageResponseBackupDto;
import com.codeit.hrbank.domain.backup.entity.BackupStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
@Tag(name = "Backup", description = "Backup API")
public interface BackupControllerDocs {
    @Operation(summary = "전체 백업 목록 조회")
    @ApiResponses (
            {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Backup 목록 조회 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CursorPageResponseBackupDto.class),
                                    examples = @ExampleObject(
                                            name = "Backup 목록 조회 성공 예시",
                                            value = """
                                                    {
                                                        "content":[
                                                        {
                                                            "id" : 36
                                                            "worker" : "1.5.5.7",
                                                            "startedAt" : "2024-04-05",
                                                            "endedAt" : "2024-05-05",
                                                            "status" : "COMPLETED",
                                                             "fileId" : 15
                                                        },
                                                        {
                                                            "id" : 24
                                                            "worker" : "8.8.8.8",
                                                            "startedAt" : "1999-06-19",
                                                            "endedAt" : "2110-06-19",
                                                            "status" : "FAILED",
                                                            "fileId" : null
                                                        }
              
                                                        ],
                                                        "nextCursor": "2023-02-19",
                                                        "nextIdAfter": 49,
                                                        "size": 4,
                                                         "totalElements": 12,
                                                         "hasNext": true
                                                    }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "에러 발생시 응답입니다",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    @GetMapping("")
    ResponseEntity<CursorPageResponseBackupDto> getBackupPage(@RequestParam(required = false) String worker
            , @RequestParam(required = false) BackupStatus status, @RequestParam(required = false) LocalDateTime startedAtFrom,
                                                              @RequestParam(required = false) LocalDateTime startedAtTo,
                                                              @RequestParam(required = false) String sortDirection,
                                                              @RequestParam(required = false) String sortField,
                                                              @RequestParam(required = false) int size
    );

    @Operation(summary = "백업 생성")
    @ApiResponses (
            {
                    @ApiResponse(
                            responseCode = "200",
                            description = "백업 생성 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema( implementation = BackupDto.class),
                                    examples = @ExampleObject(
                                            name = "Backup 생성 성공 예시",
                                            value = """
                                                      {
                                                            "id" : 36
                                                            "worker" : "1.5.5.7",
                                                            "startedAt" : "2024-04-05",
                                                            "endedAt" : "2024-05-05",
                                                            "status" : "COMPLETED",
                                                             "fileId" : 15
                                                        }
                                                    """
                                    )

                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "에러 발생시 응답입니다",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    @PostMapping("")
    ResponseEntity<BackupDto> createBackup(HttpServletRequest request) throws Exception;

    @Operation(summary = "가장 최근 백업 조회")
    @ApiResponses (
            {
                    @ApiResponse(
                            responseCode = "200",
                            description = "가장 최근 백업 조회 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema( implementation = BackupDto.class),
                                    examples = @ExampleObject(
                                            name = "최근 Backup 조회 성공 예시",
                                            value = """
                                                      {
                                                            "id" : 36
                                                            "worker" : "1.5.5.7",
                                                            "startedAt" : "2024-04-05",
                                                            "endedAt" : "2024-05-05",
                                                            "status" : "COMPLETED",
                                                             "fileId" : 15
                                                        }
                                                    """
                                    )

                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "에러 발생시 응답입니다",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    @GetMapping("/latest")
    ResponseEntity<BackupDto> getLatestBackup();
}
