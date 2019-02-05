package com.github.hondaYoshitaka.model.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class XXXPostResponseDto {

    private final Long id;

    private final String name;

    private final Integer price;
}
