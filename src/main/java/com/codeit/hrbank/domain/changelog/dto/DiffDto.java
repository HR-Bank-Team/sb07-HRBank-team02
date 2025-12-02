package com.codeit.hrbank.domain.changelog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
public class DiffDto {
    public enum PropertyType{
        이름, 이메일, 부서명, 직함, 입사일, 상태
    }
    private String propertyName;
    private String before;
    private String after;


}
