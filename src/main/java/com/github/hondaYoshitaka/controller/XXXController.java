package com.github.hondaYoshitaka.controller;

import com.github.hondaYoshitaka.model.form.XXXsFetchForm;
import com.github.hondaYoshitaka.model.form.XXXPostForm;
import com.github.hondaYoshitaka.model.response.XXXFetchResponseDto;
import com.github.hondaYoshitaka.model.response.XXXPostResponseDto;
import com.github.hondaYoshitaka.model.response.XXXsFetchResponseDto;
import com.github.hondaYoshitaka.service.XXXService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * xxxに関するcontrollerクラス.
 *
 * @author hondaYoshitaka
 */
@RestController
@RequestMapping(value = "api", produces = {MediaType.APPLICATION_JSON_VALUE})
public class XXXController {

    private final XXXService xxxService;

    public XXXController(final XXXService xxxService) {
        this.xxxService = xxxService;
    }

    @GetMapping("xxx/{id}")
    public XXXFetchResponseDto fetchXXX(
            @PathVariable final Long id
    ) {
        return xxxService.fetchOne(id);
    }

    @GetMapping("xxx")
    public XXXsFetchResponseDto fetchXXXs(
            @ModelAttribute @Validated final XXXsFetchForm form,
            final Pageable pageable
    ) {
        return xxxService.fetchAll(form.getCategoryId(),
                                   form.getPrice(),
                                   pageable);
    }

    @PostMapping("xxx")
    public XXXPostResponseDto postXXX(
            @RequestBody final XXXPostForm form
    ) {
        return xxxService.postOne(form.getName(),
                                  form.getCategoryId(),
                                  form.getPrice());
    }
}
