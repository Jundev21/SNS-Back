package com.sns.sns.service.domain.exception;


import com.sns.sns.service.common.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(BasicException.class)
    public ResponseEntity<?> applicationHandler(BasicException basic){
        log.error("Error occurs{}", basic.toString());
        return ResponseEntity.status(basic.getErrorCode().getStatus())
                .body(Response.error(basic.getErrorCode().getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> applicationHandler(RuntimeException basic){
        log.error("Error occurs{}", basic.toString());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Response.error("INTERNAL_SERVER_ERROR"));
    }
}
