package com.codeit.hrbank.domain.changelog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdatedFieldDetail {
    public enum PropertyType{
        이름, 이메일, 부서명, 직함, 입사일, 상태
    }
    private PropertyType propertyType;
    private String before;
    private String after;


}
