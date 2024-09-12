package com.example.fashionforecastbackend.global.error.exception;

import com.example.fashionforecastbackend.global.error.ErrorCode;
import com.example.fashionforecastbackend.global.error.GeneralException;

public class InvalidJwtException extends GeneralException {

	public InvalidJwtException(final ErrorCode errorCode) {
		super(errorCode);
	}
}
