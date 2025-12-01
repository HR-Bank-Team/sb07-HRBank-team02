package com.codeit.hrbank.domain.changelog.controller;

import com.codeit.hrbank.domain.changelog.dto.ChangeLogFilter;
import com.codeit.hrbank.domain.changelog.dto.CursorPageResponseChangeLogDto;
import com.codeit.hrbank.domain.changelog.sevice.ChangeLogService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/change-logs")
@RequiredArgsConstructor
public class ChangeLogController {

    private final ChangeLogService changeLogService;

    @GetMapping
    public ResponseEntity<CursorPageResponseChangeLogDto> getChangeLogs(
            @ModelAttribute ChangeLogFilter changeLogFilter
            ){

        return null;
    }
}
