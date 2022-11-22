package com.example.backend.global.exception;

import com.example.backend.dto.ErrorDTO;
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
    @ExceptionHandler(InvalidUserInputException.class)
    public ErrorDTO handleBadRequest(InvalidUserInputException ex){
        return ErrorDTO.builder()
                .errorCode(ex.getExceptionType().getErrorCode())
                .errorMessage(ex.getExceptionType().getMessage())
                .build();
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED) //401
    @ExceptionHandler(UnAuthorizedException.class)
    public ErrorDTO handleUnAuthorized(UnAuthorizedException ex){
        return ErrorDTO.builder()
                .errorCode(ex.getExceptionType().getErrorCode())
                .errorMessage(ex.getExceptionType().getMessage())
                .build();
    }

    @ResponseStatus(HttpStatus.FORBIDDEN) //403
    @ExceptionHandler(ForbiddenException.class) //TODO
    public ErrorDTO handleForbidden(ForbiddenException ex){
        return ErrorDTO.builder()
                .errorCode(ex.getExceptionType().getErrorCode())
                .errorMessage(ex.getExceptionType().getMessage())
                .build();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND) //404
    @ExceptionHandler(EntityNotExistsException.class)
    public ErrorDTO handleNotFound(EntityNotExistsException ex){
        return ErrorDTO.builder()
                .errorCode(ex.getExceptionType().getErrorCode())
                .errorMessage(ex.getExceptionType().getMessage())
                .build();
    }

    //그 외에 놓친 예외들
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorDTO handleEtc(Exception ex){
        log.error("Exception: {}", ex);
        return  ErrorDTO.builder()
                .errorCode(500)
                .errorMessage(ex.getMessage()).build();
    }
}
