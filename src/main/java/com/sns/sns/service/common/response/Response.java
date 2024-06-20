package com.sns.sns.service.common.response;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Response<T>{

    private String responseCode;
    private T responseBody;

    public static <T> Response<T> error(String errorCode){
        return new Response<>(errorCode, null);
    }
    public static <T> Response<T> success(T resultBody){
        return new Response<>("Success", resultBody);
    }

    public String toStream() {
        if(responseBody == null){
            return "{" + " responseBody" + null + "}";
        }
        return "{" + " responseBody" + responseBody + "}";
    }

}
