package com.formmaker.fff.common.exception;

import com.formmaker.fff.common.response.ResponseMessage;
//import com.formmaker.fff.common.response.ValidMessageResponse;
import com.formmaker.fff.common.response.ValidMessageResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({CustomException.class})
    protected ResponseEntity handleCustomException(CustomException ex) {
        return new ResponseEntity(new ResponseMessage(ex.getErrorCode().getMsg(), ex.getErrorCode().getStatusCode(), null)
                , HttpStatus.OK);
    }

    @ExceptionHandler({CustomValidException.class})
    protected ResponseEntity handleCustomValidException(CustomValidException ex){
        return new ResponseEntity(new ResponseMessage(ex.getNumber()+"번 문제의 "+ex.getFieldName()+" 비었습니다.",400),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidMessageResponse> methodValidException (MethodArgumentNotValidException e){
        ValidMessageResponse errorResponse = ValidMessageResponse.makeErrorResponse(e.getBindingResult());
        return new ResponseEntity<ValidMessageResponse>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
