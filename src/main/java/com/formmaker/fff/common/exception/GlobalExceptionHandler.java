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

    @ExceptionHandler({Exception.class})
    protected ResponseEntity handleServerException(Exception ex) {
        return new ResponseEntity(new ResponseMessage(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), "error")
                , HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidMessageResponse> methodValidException (MethodArgumentNotValidException e, HttpServletRequest request){
            ValidMessageResponse errorResponse = ValidMessageResponse.makeErrorResponse(e.getBindingResult());
            return new ResponseEntity<ValidMessageResponse>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }
