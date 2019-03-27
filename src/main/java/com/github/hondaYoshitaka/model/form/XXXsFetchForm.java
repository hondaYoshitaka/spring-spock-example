package com.github.hondaYoshitaka.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(description = "xxx取得のform")
public class XXXsFetchForm {
    @NotNull
    @ApiModelProperty(value = "カテゴリID", required = true)
    private Long categoryId;

    @NotNull
    @Range(min = 0, max = 1_000_000)
    @ApiModelProperty(value = "価格", required = true)
    private Integer price;
}
