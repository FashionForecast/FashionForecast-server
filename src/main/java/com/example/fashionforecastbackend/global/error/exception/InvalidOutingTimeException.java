package com.example.fashionforecastbackend.global.error.exception;

import com.example.fashionforecastbackend.global.error.ErrorCode;
import com.example.fashionforecastbackend.global.error.GeneralException;

public class InvalidOutingTimeException extends GeneralException {
	public InvalidOutingTimeException(ErrorCode errorCode) {
		super(errorCode);
	}
}
