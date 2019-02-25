package com.github.hondaYoshitaka.component.advice;

import com.github.hondaYoshitaka.component.exception.ResourceNotFoundException;
import com.github.hondaYoshitaka.model.response.ErrorResponseDto;
import com.github.hondaYoshitaka.model.response.ErrorResponseDto.ErrorDto;
import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.method.annotation.MethodArgumentConversionNotSupportedException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class ExceptionControllerAdvice extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            final HttpRequestMethodNotSupportedException ex,
            final HttpHeaders headers,
            final HttpStatus status,
            final WebRequest request
    ) {
        final var supportedMethods = ex.getSupportedHttpMethods();

        if (!CollectionUtils.isEmpty(supportedMethods)) {
            headers.setAllow(supportedMethods);
        }
        final var body = createErrorResponseDto(ex.getMessage());

        return handleExceptionInternal(ex, body, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            final HttpMediaTypeNotSupportedException ex,
            final HttpHeaders headers,
            final HttpStatus status,
            final WebRequest request
    ) {
        final var mediaTypes = ex.getSupportedMediaTypes();
        if (!CollectionUtils.isEmpty(mediaTypes)) {
            headers.setAccept(mediaTypes);
        }
        final var body = createErrorResponseDto(ex.getMessage());

        return handleExceptionInternal(ex, body, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        final var body = createErrorResponseDto(ex.getMessage());

        return handleExceptionInternal(ex, body, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        final var body = createErrorResponseDto(ex.getMessage());

        return handleExceptionInternal(ex, body, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        final var body = createErrorResponseDto(ex.getMessage());

        return handleExceptionInternal(ex, body, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        final var body = createErrorResponseDto(ex.getMessage());

        return handleExceptionInternal(ex, body, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleConversionNotSupported(ConversionNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        final var body = createErrorResponseDto(ex.getMessage());

        return handleExceptionInternal(ex, body, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(
            final TypeMismatchException ex,
            final HttpHeaders headers,
            final HttpStatus status,
            final WebRequest request
    ) {
        final var builder = ErrorDto.builder().code(ex.getErrorCode());

        if (ex instanceof MethodArgumentTypeMismatchException) {
            builder.field(((MethodArgumentTypeMismatchException) ex).getName());

        } else if (ex instanceof MethodArgumentConversionNotSupportedException) {
            builder.field(((MethodArgumentConversionNotSupportedException) ex).getName());

        } else {
            builder.field(ex.getPropertyName());
        }
        final var body = createErrorResponseDto(ex.getMessage(), ImmutableList.of(builder.build()));

        return handleExceptionInternal(ex, body, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            final HttpMessageNotReadableException ex,
            final HttpHeaders headers,
            final HttpStatus status,
            final WebRequest request
    ) {
        final var body = createErrorResponseDto(ex.getMessage());

        return handleExceptionInternal(ex, body, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(
            final HttpMessageNotWritableException ex,
            final HttpHeaders headers,
            final HttpStatus status,
            final WebRequest request
    ) {
        final var body = createErrorResponseDto(ex.getMessage());

        return handleExceptionInternal(ex, body, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            final MethodArgumentNotValidException ex,
            final HttpHeaders headers,
            final HttpStatus status,
            final WebRequest request
    ) {
        final var errors = ex.getBindingResult().getFieldErrors().stream()
                             .map(e -> ErrorDto.builder().code(e.getCode()).field(e.getField()).build())
                             .collect(Collectors.toList());
        final var body = createErrorResponseDto(ex.getMessage(), errors);

        return handleExceptionInternal(ex, body, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(
            final MissingServletRequestPartException ex,
            final HttpHeaders headers,
            final HttpStatus status,
            final WebRequest request
    ) {
        final var body = createErrorResponseDto(ex.getMessage());

        return handleExceptionInternal(ex, body, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(
            final BindException ex,
            final HttpHeaders headers,
            final HttpStatus status,
            final WebRequest request
    ) {
        final var errors = ex.getBindingResult().getFieldErrors().stream()
                             .map(e -> ErrorDto.builder().code(e.getCode()).field(e.getField()).build())
                             .collect(Collectors.toList());
        final var body = createErrorResponseDto(ex.getMessage(), errors);

        return handleExceptionInternal(ex, body, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            final NoHandlerFoundException ex,
            final HttpHeaders headers,
            final HttpStatus status,
            final WebRequest request
    ) {
        final var body = createErrorResponseDto(ex.getMessage());

        return handleExceptionInternal(ex, body, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleAsyncRequestTimeoutException(
            final AsyncRequestTimeoutException ex,
            final HttpHeaders headers,
            final HttpStatus status,
            final WebRequest webRequest
    ) {
        if (webRequest instanceof ServletWebRequest) {
            final var servletWebRequest = (ServletWebRequest) webRequest;
            final var response = servletWebRequest.getResponse();

            if (response != null && response.isCommitted()) {
                log.warn("Async request timed out");

                return null;
            }
        }
        final var body = createErrorResponseDto(ex.getMessage());

        return handleExceptionInternal(ex, body, headers, status, webRequest);
    }

    @ExceptionHandler({ResourceNotFoundException.class})
    public final ResponseEntity<Object> handleResourceNotFoundException(final ResourceNotFoundException ex,
                                                                        final WebRequest request) {
        final var body = createErrorResponseDto(ex.getMessage());

        return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler({Throwable.class})
    public final ResponseEntity<Object> handleThrowable(final Throwable t) {
        log.error("Unknown error occurred", t);

        final var body = createErrorResponseDto(t.getMessage());

        return new ResponseEntity<>(body,
                                    new HttpHeaders(),
                                    HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * @param message エラーメッセージ
     * @return response dto
     */
    private static ErrorResponseDto createErrorResponseDto(final String message) {
        return createErrorResponseDto(message, Collections.emptyList());
    }

    /**
     * @param message エラーメッセージ
     * @param errors  詳細なエラー
     * @return response dto
     */
    private static ErrorResponseDto createErrorResponseDto(final String message,
                                                           final List<ErrorDto> errors) {
        return ErrorResponseDto.builder()
                               .timestamp(LocalDateTime.now())
                               .message(message)
                               .errors(errors)
                               .build();
    }
}
