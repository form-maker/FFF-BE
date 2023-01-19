package com.formmaker.fff.common.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.formmaker.fff.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.validation.BindingResult;

import java.util.Objects;

@Getter
@AllArgsConstructor
public class ValidMessageResponse {

    private String msg;
    private Integer statusCode;


    public static ValidMessageResponse makeErrorResponse(BindingResult bindingResult) {
        Integer statusCode= 0 ;
        String msg = "";

        //에러가 있다면
        if (bindingResult.hasErrors()) {
            //DTO에 설정한 message값을 가져온다.
            msg = bindingResult.getFieldError().getDefaultMessage();

            //DTO에 유효성체크를 걸어놓은 어노테이션명을 가져온다.
            String bindResultCode = bindingResult.getFieldError().getCode();

            switch (Objects.requireNonNull(bindResultCode)) {
                case "NotNull" :
                    statusCode = ErrorCode.NOT_NULL.getStatusCode();
                    break;
                case "NotBlank":
                    statusCode = ErrorCode.NOT_NULL.getStatusCode();
                    break;
                case "Pattern":
                    statusCode = ErrorCode.PATTERN.getStatusCode();
                    break;
                case "Email":
                    statusCode = ErrorCode.EMAIL.getStatusCode();
                    break;
            }
        }
        return new ValidMessageResponse(msg , statusCode);
    }
}
