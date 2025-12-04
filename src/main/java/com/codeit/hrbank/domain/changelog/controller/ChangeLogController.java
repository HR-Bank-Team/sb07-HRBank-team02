package com.codeit.hrbank.domain.changelog.controller;

import com.codeit.hrbank.domain.changelog.controller.docs.ChangeLogControllerDocs;
import com.codeit.hrbank.domain.changelog.dto.ChangeLogFilter;
import com.codeit.hrbank.domain.changelog.dto.CursorPageResponseChangeLogDto;
import com.codeit.hrbank.domain.changelog.dto.DiffDto;
import com.codeit.hrbank.domain.changelog.service.ChangeLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/change-logs")
@RequiredArgsConstructor
public class ChangeLogController implements ChangeLogControllerDocs {

    private final ChangeLogService changeLogService;


    @GetMapping
    public ResponseEntity<CursorPageResponseChangeLogDto> getChangeLogs(
            @ModelAttribute ChangeLogFilter changeLogFilter
    ) {
        CursorPageResponseChangeLogDto cursorPageResponseChangeLogDto = changeLogService.getChangeLogs(changeLogFilter);
        return ResponseEntity.status(HttpStatus.OK).body(cursorPageResponseChangeLogDto);
    }

    @GetMapping("/{id}/diffs")
    public ResponseEntity<List<DiffDto>> getChangeLogDetails(
            @PathVariable Long id) {
        List<DiffDto> diffsByChannelLogId = changeLogService.getDiffsByChannelLogId(id);
        return ResponseEntity.status(HttpStatus.OK).body(diffsByChannelLogId);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getLogCount(
            @RequestParam(required = false) OffsetDateTime fromDate,
            @RequestParam(required = false) OffsetDateTime toDate
    ) {
        ZoneId KST = ZoneId.of("Asia/Seoul");
        OffsetDateTime now = OffsetDateTime.now(KST);

        OffsetDateTime from = (fromDate != null) ? fromDate : now.minusDays(7);
        OffsetDateTime to = (toDate != null) ? toDate : now;

        Long count = changeLogService.countChangeLogsBetween(
                from.toLocalDateTime(),
                to.toLocalDateTime()
        );
        return ResponseEntity.status(HttpStatus.OK).body(count);
    }
}
