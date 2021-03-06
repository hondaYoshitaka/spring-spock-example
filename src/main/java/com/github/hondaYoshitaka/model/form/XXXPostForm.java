package com.github.hondaYoshitaka.model.form;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class XXXPostForm {
    @NotBlank
    @Length(max = 255)
    private String name;

    @NotNull
    private Long categoryId;

    @NotNull
    @Range(min = 0, max = 1_000_000)
    private Integer price;
}
