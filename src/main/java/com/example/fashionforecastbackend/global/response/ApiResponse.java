package com.example.fashionforecastbackend.global.response;

import lombok.Getter;

@Getter
public class ApiResponse<T> {

	private final T data;

	private final int status;

	private final String message;

	private ApiResponse(T data, int status, String message) {
		this.data = data;
		this.status = status;
		this.message = message;
	}

	public static ApiResponse<Void> ok() {
		return ok(null);
	}

	public static <T> ApiResponse<T> ok(T data) {
		return new ApiResponse<>(data, 200, "OK");
	}

	public static <T> ApiResponse<T> created(T data) {
		return new ApiResponse<>(data, 201, "CREATED");
	}

}
