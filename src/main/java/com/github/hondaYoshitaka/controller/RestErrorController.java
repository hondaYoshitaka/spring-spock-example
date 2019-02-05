package com.github.hondaYoshitaka.controller;

import com.github.hondaYoshitaka.model.response.ErrorResponseDto;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping({"${server.error.path:${error.path:/error}}"})
public class RestErrorController extends AbstractErrorController {

    @Getter
    private final String errorPath;

    public RestErrorController(final ErrorAttributes errorAttributes,
                               final List<ErrorViewResolver> errorViewResolvers,
                               @Value("${server.error.path:${error.path:/error}}") final String path) {
        super(errorAttributes, errorViewResolvers);

        this.errorPath = path;
    }

    @GetMapping
    public ResponseEntity<Object> error(final HttpServletRequest request) {
        final var body = ErrorResponseDto.builder()
                                         .timestamp(LocalDateTime.now())
                                         .message("unknown error occurred. " + getErrorAttributes(request, true))
                                         .errors(Collections.emptyList())
                                         .build();
        final var status = this.getStatus(request);

        return new ResponseEntity<>(body, status);
    }
}
