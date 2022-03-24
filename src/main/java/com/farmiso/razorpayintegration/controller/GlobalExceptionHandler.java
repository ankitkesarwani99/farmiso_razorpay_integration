package com.farmiso.razorpayintegration.controller;

import com.farmiso.razorpayintegration.response.GenericResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.ws.rs.BadRequestException;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    GenericResponse onMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {
        log.error("Bad request error:{}", e.getMessage());
        GenericResponse genericResponse = new GenericResponse();
        List<String> errors = new ArrayList<>();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            errors.add(fieldError.getDefaultMessage());
        }
        genericResponse.setErrors(errors);
        return genericResponse;
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    GenericResponse onBadRequestException(
            BadRequestException e) {
        log.error("Bad request error:{},trace:{}", e.getMessage(), ExceptionUtils.getStackTrace(e));
        GenericResponse genericResponse = new GenericResponse();
        List<String> errors = new ArrayList<>();
        errors.add(e.getMessage());
        genericResponse.setErrors(errors);
        return genericResponse;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    GenericResponse onGenericException(Exception e) {
        log.error("some unknown error occurred error:{},trace:{}", e.getMessage(), ExceptionUtils.getStackTrace(e));
        GenericResponse genericResponse = new GenericResponse();
        List<String> errors = new ArrayList<>();
        errors.add(e.getMessage());
        genericResponse.setErrors(errors);
        return genericResponse;
    }

}
