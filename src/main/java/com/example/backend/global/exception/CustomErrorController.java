package com.example.backend.global.exception;

import com.example.backend.dto.ErrorDTO;
import com.example.backend.global.exception.base.CustomException;
import com.example.backend.global.exception.comment.CommentInvalidInputException;
import com.example.backend.global.exception.social.SocialInvalidInputException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class CustomErrorController implements ErrorController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorDTO handleBadRequest(MethodArgumentNotValidException ex){
        List<FieldError> errors = ex.getBindingResult().getFieldErrors();
        StringBuilder sb = new StringBuilder();
        for(FieldError error : errors){
            sb.append("'");
            sb.append(error.getField());
            sb.append("' ");
        }
        sb.append("validation 에러 입니다.");

        return ErrorDTO.builder()
                .errorCode(-1) //validation errorCode?
                .errorMessage(sb.toString())
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST) //400
    @ExceptionHandler(value = {InvalidUserInputException.class, CommentInvalidInputException.class,
            SocialInvalidInputException.class})
    public ErrorDTO handleBadRequest(CustomException ex){
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
