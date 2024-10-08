package com.webank.wsdaw.keystore.advice;

import com.webank.wsdaw.keystore.enums.CodeEnum;
import com.webank.wsdaw.keystore.vo.response.CommonResponse;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponse> methodArgumentNotValidException(
            MethodArgumentNotValidException ex) {
        log.error("ONError: methodArgumentNotValidException exception", ex);
        BindingResult bindingResult = ex.getBindingResult();
        return ResponseEntity.ok(
                CommonResponse.error(
                        CodeEnum.REQUEST_PARAMS_ERROR.getCode(),
                        Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage()));
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<CommonResponse> unhandledException(Exception ex) {
        log.error("ONError: unhandled exception", ex);
        return ResponseEntity.ok(CommonResponse.error(CodeEnum.UNKNOWN_ERROR));
    }
}
