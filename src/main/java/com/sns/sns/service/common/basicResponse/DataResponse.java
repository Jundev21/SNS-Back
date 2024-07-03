package com.sns.sns.service.common.basicResponse;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class DataResponse<T> extends BasicResponse {

	private final T responseBody;

	public DataResponse(Integer statusCode, String message, T responseData) {
		super(statusCode, message);
		this.responseBody = responseData;
	}

	public static <T> DataResponse<T> successBodyResponse(HttpStatus httpStatus, T responseData) {
		return new DataResponse<>(httpStatus.value(), httpStatus.getReasonPhrase(), responseData);
	}

	public static <T> DataResponse<T> failBodyResponse(HttpStatus httpStatus, T responseData) {
		return new DataResponse<>(httpStatus.value(), httpStatus.getReasonPhrase(), responseData);
	}
}
