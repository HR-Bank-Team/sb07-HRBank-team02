package com.codeit.hrbank.domain.department.dto;


import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

// NOTE: @JsonCreator는 @RequestBody(요청 바디)에만 동작하고 쿼리파라미터 바인딩에는 아무 영향주지않기 때문에 컨버터 클래스 ConversionService에 등록
@Component
public class SortDirectionConverter implements Converter<String, SortDirection> {
    @Override
    public SortDirection convert(String source) {
        return SortDirection.from(source);
    }
}
