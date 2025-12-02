package com.codeit.hrbank.domain.changelog.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;


import java.time.LocalDateTime;


@Getter
@AllArgsConstructor
public class ChangeLogFilter {

    private String employeeNumber;
    private String type;   // 로그 타입 필터 (CREATED, UPDATED 등)
    private String memo;          // 메모 내용 필터
    private String ipAddress;

    private String sortField;       // 정렬 필드, 기본값: at
    private String sortDirection;  // 정렬 방향, 기본값: desc-> 최신순
    private Integer size;           // 조회 개수, 기본값: 30

    private LocalDateTime atFrom;
    private LocalDateTime atTo;

    private String cursor; // 시간 기준일 땐 time, ip기준일 땐 id
    private Long idAfter;  // 커서와 동일한 at값에서 id 기준으로 다음 페이지 시작

}
