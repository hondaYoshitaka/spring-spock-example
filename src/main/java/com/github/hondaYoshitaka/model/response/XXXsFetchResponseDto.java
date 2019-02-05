package com.github.hondaYoshitaka.model.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class XXXsFetchResponseDto {

    private final List<XXXDto> xxxs;

    @Builder
    @Getter
    public static class XXXDto {
        private final Long id;

        private final String name;

        private final Integer price;
    }
}
