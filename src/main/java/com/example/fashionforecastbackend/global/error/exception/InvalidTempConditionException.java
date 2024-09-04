package com.example.fashionforecastbackend.global.error.exception;

import com.example.fashionforecastbackend.global.error.ErrorCode;
import com.example.fashionforecastbackend.global.error.GeneralException;

public class InvalidTempConditionException extends GeneralException {
	public InvalidTempConditionException(ErrorCode errorCode) {
		super(errorCode);
	}
}
