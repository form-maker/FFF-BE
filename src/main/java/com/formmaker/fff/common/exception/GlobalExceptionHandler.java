package com.formmaker.fff.common.exception;

import com.formmaker.fff.common.response.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler{

    @ExceptionHandler({CustomException.class})
    protected ResponseEntity handleCustomException(CustomException ex){
        return new ResponseEntity( new ResponseMessage(ex.getErrorCode().getMsg(), ex.getErrorCode().getStatusCode(), ex.getErrorCode() )
                , HttpStatus.OK);
    }

    @ExceptionHandler({Exception.class})
    protected ResponseEntity handleServerException(Exception ex){
        return new ResponseEntity(new ResponseMessage(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), "error")
                , HttpStatus.INTERNAL_SERVER_ERROR);
    }
}