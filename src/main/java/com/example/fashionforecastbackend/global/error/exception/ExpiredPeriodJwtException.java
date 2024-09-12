package com.example.fashionforecastbackend.global.error.exception;

import com.example.fashionforecastbackend.global.error.ErrorCode;
import com.example.fashionforecastbackend.global.error.GeneralException;

public class ExpiredPeriodJwtException extends GeneralException {

	public ExpiredPeriodJwtException(final ErrorCode errorCode) {
		super(errorCode);
	}
}
