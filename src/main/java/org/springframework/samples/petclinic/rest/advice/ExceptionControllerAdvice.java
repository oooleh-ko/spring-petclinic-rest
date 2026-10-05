/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic.rest.advice;

import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.samples.petclinic.rest.dto.ValidationMessageDto;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * Global Exception handler for REST controllers.
 * <p>
 * This class handles exceptions thrown by REST controllers and returns appropriate HTTP responses to the client.
 * Client mistakes get a 4xx status; only unexpected failures end up as 500.
 *
 * @author Vitaliy Fedoriv
 * @author Alexander Dudkin
 */
@ControllerAdvice
public class ExceptionControllerAdvice {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionControllerAdvice.class);
    private static final String ERROR_UNEXPECTED = "An unexpected error occurred while processing your request";
    private static final String ERROR_DATA_INTEGRITY = "The requested resource could not be processed due to a data constraint violation";
    private static final String ERROR_INVALID_REQUEST = "The request contains invalid or missing parameters";
    private static final String ERROR_UNREADABLE_BODY = "The request body is missing or is not valid JSON";
    private static final String ERROR_NOT_FOUND = "The requested resource was not found";
    private static final String ERROR_METHOD_NOT_ALLOWED = "The HTTP method is not supported for this resource";
    private static final String ERROR_UNSUPPORTED_MEDIA_TYPE = "The request content type is not supported";

    /**
     * Private method for constructing the {@link ProblemDetail} object passing the name and details of the exception
     * class.
     *
     * @param e     Object referring to the thrown exception.
     * @param status HTTP response status.
     * @param url URL request.
     */
    private ProblemDetail detailBuild(Exception e, HttpStatus status, StringBuffer url, String detail) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(status);
        problemDetail.setType(URI.create(url.toString()));
        problemDetail.setTitle(e.getClass().getSimpleName());
        problemDetail.setDetail(detail);
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setProperty("schemaValidationErrors", List.<ValidationMessageDto>of());
        return problemDetail;
    }

    /**
     * Describes one invalid field or parameter in the format used by {@code schemaValidationErrors}.
     */
    private ValidationMessageDto validationMessage(String field, String defaultMessage, Object rejectedValue) {
        String rejected = Objects.toString(rejectedValue, "null");
        String message = Objects.toString(defaultMessage, "Validation failed");
        return new ValidationMessageDto("Field '%s' %s (rejected value: %s)".formatted(field, message, rejected))
            .putAdditionalProperty("field", field)
            .putAdditionalProperty("rejectedValue", rejected)
            .putAdditionalProperty("defaultMessage", message);
    }

    /**
     * Handles all general exceptions by returning a 500 Internal Server Error status with error details.
     *
     * @param e The {@link Exception} to be handled
     * @param request {@link HttpServletRequest} object referring to the current request.
     * @return A {@link ResponseEntity} containing the error information and a 500 Internal Server Error status
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<ProblemDetail> handleGeneralException(Exception e, HttpServletRequest request) {
        logger.error("Unexpected error at {} {}", request.getMethod(), request.getRequestURI(), e);
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ProblemDetail detail = this.detailBuild(e, status, request.getRequestURL(), ERROR_UNEXPECTED);
        return ResponseEntity.status(status).body(detail);
    }

    /**
     * Handles {@link DataIntegrityViolationException} which typically indicates database constraint violations. This
     * method returns a 404 Not Found status if an entity does not exist.
     *
     * @param e The {@link DataIntegrityViolationException} to be handled
     * @param request {@link HttpServletRequest} object referring to the current request.
     * @return A {@link ResponseEntity} containing the error information and a 404 Not Found status
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseBody
    public ResponseEntity<ProblemDetail> handleDataIntegrityViolationException(DataIntegrityViolationException e, HttpServletRequest request) {
        logger.warn("Data integrity violation at {} {}: {}",
            request.getMethod(),
            request.getRequestURI(),
            e.getMessage());
        logger.debug("Data integrity violation stacktrace", e);
        HttpStatus status = HttpStatus.NOT_FOUND;
        ProblemDetail detail = this.detailBuild(e, status, request.getRequestURL(), ERROR_DATA_INTEGRITY);
        return ResponseEntity.status(status).body(detail);
    }

    /**
     * Handles exception thrown by Bean Validation on request bodies annotated with {@code @Valid}.
     *
     * @param e The {@link MethodArgumentNotValidException} to be handled
     * @param request {@link HttpServletRequest} object referring to the current request.
     * @return A {@link ResponseEntity} containing the error information and a 400 Bad Request status.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<ProblemDetail> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ProblemDetail detail = this.detailBuild(e, status, request.getRequestURL(), ERROR_INVALID_REQUEST);
        List<ValidationMessageDto> schemaValidationErrors = e.getBindingResult().getFieldErrors().stream()
            .map(fieldError -> validationMessage(
                fieldError.getField(), fieldError.getDefaultMessage(), fieldError.getRejectedValue()))
            .toList();
        logger.debug("Validation error at {} {}: {}",
            request.getMethod(),
            request.getRequestURI(),
            e.getBindingResult().getFieldErrors());
        detail.setProperty("schemaValidationErrors", schemaValidationErrors);
        return ResponseEntity.status(status).body(detail);
    }

    /**
     * Handles constraint violations on path variables and request parameters, e.g. a negative id
     * ({@code @Min(0)}) or a page size outside {@code @Min(1) @Max(100)}.
     *
     * @param e The {@link ConstraintViolationException} to be handled
     * @param request {@link HttpServletRequest} object referring to the current request.
     * @return A {@link ResponseEntity} containing the error information and a 400 Bad Request status.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public ResponseEntity<ProblemDetail> handleConstraintViolationException(ConstraintViolationException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ProblemDetail detail = this.detailBuild(e, status, request.getRequestURL(), ERROR_INVALID_REQUEST);
        List<ValidationMessageDto> schemaValidationErrors = e.getConstraintViolations().stream()
            .map(violation -> validationMessage(
                parameterName(violation), violation.getMessage(), violation.getInvalidValue()))
            .toList();
        logger.debug("Invalid parameters at {} {}: {}", request.getMethod(), request.getRequestURI(), e.getMessage());
        detail.setProperty("schemaValidationErrors", schemaValidationErrors);
        return ResponseEntity.status(status).body(detail);
    }

    /**
     * The violation path looks like {@code getOwner.ownerId}; its last node is the parameter name.
     */
    private String parameterName(ConstraintViolation<?> violation) {
        String name = null;
        for (Path.Node node : violation.getPropertyPath()) {
            name = node.getName();
        }
        return name;
    }

    /**
     * Handles a path variable or request parameter of the wrong type, e.g. {@code /api/owners/abc}.
     *
     * @param e The {@link MethodArgumentTypeMismatchException} to be handled
     * @param request {@link HttpServletRequest} object referring to the current request.
     * @return A {@link ResponseEntity} containing the error information and a 400 Bad Request status.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseBody
    public ResponseEntity<ProblemDetail> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ProblemDetail detail = this.detailBuild(e, status, request.getRequestURL(), ERROR_INVALID_REQUEST);
        String expectedType = e.getRequiredType() == null ? "a different type" : e.getRequiredType().getSimpleName();
        detail.setProperty("schemaValidationErrors",
            List.of(validationMessage(e.getName(), "must be of type " + expectedType, e.getValue())));
        return ResponseEntity.status(status).body(detail);
    }

    /**
     * Handles a request body that is missing or cannot be parsed as JSON.
     *
     * @param e The {@link HttpMessageNotReadableException} to be handled
     * @param request {@link HttpServletRequest} object referring to the current request.
     * @return A {@link ResponseEntity} containing the error information and a 400 Bad Request status.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public ResponseEntity<ProblemDetail> handleHttpMessageNotReadableException(HttpMessageNotReadableException e, HttpServletRequest request) {
        logger.debug("Unreadable request body at {} {}: {}", request.getMethod(), request.getRequestURI(), e.getMessage());
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ProblemDetail detail = this.detailBuild(e, status, request.getRequestURL(), ERROR_UNREADABLE_BODY);
        return ResponseEntity.status(status).body(detail);
    }

    /**
     * Handles requests to a URL that no controller or static resource serves.
     *
     * @param e The {@link NoResourceFoundException} to be handled
     * @param request {@link HttpServletRequest} object referring to the current request.
     * @return A {@link ResponseEntity} containing the error information and a 404 Not Found status.
     */
    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseBody
    public ResponseEntity<ProblemDetail> handleNoResourceFoundException(NoResourceFoundException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ProblemDetail detail = this.detailBuild(e, status, request.getRequestURL(), ERROR_NOT_FOUND);
        return ResponseEntity.status(status).body(detail);
    }

    /**
     * Handles an HTTP method that the URL does not support; the {@code Allow} header lists the supported ones.
     *
     * @param e The {@link HttpRequestMethodNotSupportedException} to be handled
     * @param request {@link HttpServletRequest} object referring to the current request.
     * @return A {@link ResponseEntity} containing the error information and a 405 Method Not Allowed status.
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public ResponseEntity<ProblemDetail> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.METHOD_NOT_ALLOWED;
        ProblemDetail detail = this.detailBuild(e, status, request.getRequestURL(), ERROR_METHOD_NOT_ALLOWED);
        return ResponseEntity.status(status).headers(e.getHeaders()).body(detail);
    }

    /**
     * Handles a request body in a format other than JSON; the {@code Accept} header lists the supported ones.
     *
     * @param e The {@link HttpMediaTypeNotSupportedException} to be handled
     * @param request {@link HttpServletRequest} object referring to the current request.
     * @return A {@link ResponseEntity} containing the error information and a 415 Unsupported Media Type status.
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseBody
    public ResponseEntity<ProblemDetail> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.UNSUPPORTED_MEDIA_TYPE;
        ProblemDetail detail = this.detailBuild(e, status, request.getRequestURL(), ERROR_UNSUPPORTED_MEDIA_TYPE);
        return ResponseEntity.status(status).headers(e.getHeaders()).body(detail);
    }

}
