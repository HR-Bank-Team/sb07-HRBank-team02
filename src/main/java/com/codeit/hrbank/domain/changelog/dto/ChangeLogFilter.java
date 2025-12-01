package com.codeit.hrbank.domain.changelog.dto;

import com.codeit.hrbank.domain.changelog.entity.ChangeLogType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeLogFilter {

    // 선택적 필터
    private ChangeLogType type;   // 로그 타입 필터 (CREATED, UPDATED 등)
    private String memo;          // 메모 내용 필터
    private String employeeNumber; // 직원 번호 필터

    // 정렬/페이징
    private String sortField = "at";       // 정렬 필드, 기본값: at
    private String sortDirection = "desc"; // 정렬 방향, 기본값: desc
    private int size = 30;                 // 조회 개수, 기본값: 30

    // 커서 기반 페이징
    private String cursor; // 다음 페이지 시작 기준 timestamp (ISO 8601 문자열)
    private Long idAfter;  // 커서와 동일한 at값에서 id 기준으로 다음 페이지 시작
}
