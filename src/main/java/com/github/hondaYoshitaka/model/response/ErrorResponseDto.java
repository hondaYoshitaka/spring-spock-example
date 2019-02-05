package com.github.hondaYoshitaka.model.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
public class ErrorResponseDto {

    private final LocalDateTime timestamp;

    private final String message;

    private final List<ErrorDto> errors;

    @Builder
    @Getter
    public static class ErrorDto {

        private final String code;

        private final String field;
    }
}
