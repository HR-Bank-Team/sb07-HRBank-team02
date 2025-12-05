package com.codeit.hrbank.global.exception;

import java.time.LocalDateTime;

public record ErrorResponse(

        LocalDateTime timestamp,
        Integer status,
        String message,
        String details

) {
}
