package com.codeit.hrbank.domain.changelog.controller;

import com.codeit.hrbank.domain.changelog.dto.ChangeLogFilter;
import com.codeit.hrbank.domain.changelog.dto.CursorPageResponseChangeLogDto;
import com.codeit.hrbank.domain.changelog.dto.DiffDto;
import com.codeit.hrbank.domain.changelog.service.ChangeLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/change-logs")
@RequiredArgsConstructor
public class ChangeLogController {

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
            @PathVariable Long id){
        List<DiffDto> diffsByChannelLogId = changeLogService.getDiffsByChannelLogId(id);
        return ResponseEntity.status(HttpStatus.OK).body(diffsByChannelLogId);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getLogCount(
            @RequestParam LocalDateTime fromDate,
            @RequestParam LocalDateTime toDate
    ){
        Long count = changeLogService.countChangeLogsBetween(fromDate, toDate);
        return ResponseEntity.status(HttpStatus.OK).body(count);
    }
}
