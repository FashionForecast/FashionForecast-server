package com.example.fashionforecastbackend.global.response;

import java.util.Map;

import lombok.Getter;

@Getter
public class ErrorResponse<T> {

	private final Map<String, T> data;

	private final String code;

	private final String message;

	private ErrorResponse(Map<String, T> data, String code, String message) {
		this.data = data;
		this.code = code;
		this.message = message;
	}

	public static ErrorResponse<Void> error(String code, String message) {
		return error(null, code, message);
	}

	public static <T> ErrorResponse<T> error(Map<String, T> data, String code, String message) {
		return new ErrorResponse<>(data, code, message);
	}

}
