package com.example.backend.global.exception;

import com.example.backend.dto.ResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class CustomErrorController implements ErrorController {

    @ResponseStatus(HttpStatus.BAD_REQUEST) //400
    @ExceptionHandler(InvalidInputException.class)
    public ResponseDTO<?> handleBadRequest(InvalidInputException ex){
        return ResponseDTO.builder().success(false).message(ex.getExceptionType().getMessage()).build();
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED) //401
    @ExceptionHandler(UnAuthorizedException.class)
    public ResponseDTO<?> handleUnAuthorized(UnAuthorizedException ex){
        return ResponseDTO.builder().success(false).message(ex.getExceptionType().getMessage()).build();
    }

    @ResponseStatus(HttpStatus.FORBIDDEN) //403
    @ExceptionHandler(ForbiddenException.class) //TODO
    public ResponseDTO<?> handleForbidden(ForbiddenException ex){
        return ResponseDTO.builder().success(false).message(ex.getExceptionType().getMessage()).build();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND) //404
    @ExceptionHandler(EntityNotExistsException.class)
    public ResponseDTO<?> handleNotFound(EntityNotExistsException ex){
        return ResponseDTO.builder().success(false).message(ex.getExceptionType().getMessage()).build();
    }

    //그 외에 놓친 예외들
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseDTO<?> handleEtc(Exception ex){
        log.error("Exception: {}", ex);
        return  ResponseDTO.builder().success(false).message(ex.getMessage()).build();
    }
}
