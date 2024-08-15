package com.example.fashionforecastbackend.global.error;

import lombok.Getter;

@Getter
public abstract class GeneralException extends RuntimeException {

	private final ErrorCode errorCode;

	protected GeneralException(ErrorCode errorCode, String message) {
		super(errorCode.getMessage() + ": " + message);
		this.errorCode = errorCode;
	}

	protected GeneralException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}

}