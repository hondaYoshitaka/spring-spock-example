package com.github.hondaYoshitaka.controller;

import com.github.hondaYoshitaka.model.form.XXXPostForm;
import com.github.hondaYoshitaka.model.response.XXXFetchResponseDto;
import com.github.hondaYoshitaka.model.response.XXXPostResponseDto;
import com.github.hondaYoshitaka.service.XXXService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api")
public class XXXController {

    private final XXXService xxxService;

    public XXXController(final XXXService xxxService) {
        this.xxxService = xxxService;
    }

    @GetMapping("xxx/{id}")
    public XXXFetchResponseDto fetchXXXs(
            @PathVariable final Long id
    ) {
        return xxxService.fetchOne(id);
    }

    @PostMapping("xxx")
    public XXXPostResponseDto postXXX(
            @RequestBody final XXXPostForm form
    ) {
        return xxxService.postXXX(form.getName(),
                                  form.getCategoryId(),
                                  form.getPrice());
    }
}
