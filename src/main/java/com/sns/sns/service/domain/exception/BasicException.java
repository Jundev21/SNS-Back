package com.sns.sns.service.domain.exception;


import lombok.Getter;

@Getter
public class BasicException extends RuntimeException{

    private String message;
    private ErrorCode errorCode;

    public BasicException(String message){
        super(message);
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    public BasicException(ErrorCode errorCode, String message){
        this.message = message;
        this.errorCode = errorCode;
    }
}
