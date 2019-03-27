package com.github.hondaYoshitaka.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel(description = "xxx登録のform")
public class XXXPostForm {
    @NotBlank
    @Length(max = 255)
    @ApiModelProperty(value = "xxx名", required = true)
    private String name;

    @NotNull
    @ApiModelProperty(value = "カテゴリID", required = true)
    private Long categoryId;

    @NotNull
    @Range(min = 0, max = 1_000_000)
    @ApiModelProperty(value = "価格", required = true)
    private Integer price;
}
