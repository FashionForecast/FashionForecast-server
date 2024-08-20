package com.example.fashionforecastbackend.global.error.exception;

import com.example.fashionforecastbackend.global.error.ErrorCode;
import com.example.fashionforecastbackend.global.error.GeneralException;

public class InvalidWeatherRequestException extends GeneralException {

	public InvalidWeatherRequestException(ErrorCode errorCode) {
		super(errorCode);
	}
}
