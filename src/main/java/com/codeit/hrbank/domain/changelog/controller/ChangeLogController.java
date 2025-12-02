package com.codeit.hrbank.domain.changelog.controller;

import com.codeit.hrbank.domain.changelog.dto.ChangeLogFilter;
import com.codeit.hrbank.domain.changelog.dto.CursorPageResponseChangeLogDto;
import com.codeit.hrbank.domain.changelog.dto.DiffDto;
import com.codeit.hrbank.domain.changelog.service.ChangeLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        changeLogService.getChangeLogs(changeLogFilter);
        return null;
    }

    @GetMapping("/{id}/diffs")
    public ResponseEntity<List<DiffDto>> getChangeLogDetails(
            @PathVariable Long id){
        return null;
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> getLogCount(
            @RequestParam String fromDate,
            @RequestParam String toDate
    ){
        return null;

    }
}
