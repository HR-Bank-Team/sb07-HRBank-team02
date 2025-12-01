package com.codeit.hrbank.domain.backup.dto.response;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ErrorDto(

        LocalDateTime timestamp,
        Long status,
        String message,
        String details

) {
}
