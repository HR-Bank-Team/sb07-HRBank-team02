package com.codeit.hrbank.changelog.unit.service;

import com.codeit.hrbank.domain.changelog.dto.ChangeLogDto;
import com.codeit.hrbank.domain.changelog.dto.ChangeLogFilter;
import com.codeit.hrbank.domain.changelog.dto.CursorPageResponseChangeLogDto;
import com.codeit.hrbank.domain.changelog.dto.DiffDto;
import com.codeit.hrbank.domain.changelog.service.ChangeLogService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@SpringBootTest
@Transactional
class ChangeLogServiceTest {

    @Autowired
    ChangeLogService changeLogService;

    @Test
    void getChangeLogsSorted() {

        ChangeLogFilter changeLogFilter = new ChangeLogFilter(
                null, null, null,
                null, "at", "asc",
                30, null, null, "2025-11-10T01:30:00Z", 10L);

        CursorPageResponseChangeLogDto changeLogsSorted = changeLogService.getChangeLogs(changeLogFilter);


        List<ChangeLogDto> content = changeLogsSorted.getContent();
        for (ChangeLogDto changeLogDto : content) {
            System.out.println("changeLogDto = " + changeLogDto);
        }
        System.out.println(changeLogsSorted.getTotalElements());
        System.out.println(changeLogsSorted.getSize());
        System.out.println(changeLogsSorted.getNextCursor());
        System.out.println(changeLogsSorted.getNextIdAfter());


    }

    @Test
    void getDiffs(){
        List<DiffDto> diffsByChannelLogId = changeLogService.getDiffsByChannelLogId(1L);
        for (DiffDto diffDto : diffsByChannelLogId) {
            System.out.println("diffDto = " + diffDto);
        }
    }

    @Test
    void countBetween(){
//        fromDate=2025-11-25T00:00:00Z&toDate=2025-12-02T23:59:59Z
        LocalDateTime fromDate=LocalDateTime.ofInstant(Instant.parse("2025-11-25T00:00:00Z"), ZoneId.systemDefault());
        LocalDateTime toDate=LocalDateTime.ofInstant(Instant.parse("2025-12-02T23:59:59Z"), ZoneId.systemDefault());
        Long countChangeLogsBetween = changeLogService.countChangeLogsBetween(fromDate, toDate);
        System.out.println("countChangeLogsBetween = " + countChangeLogsBetween);
    }
}