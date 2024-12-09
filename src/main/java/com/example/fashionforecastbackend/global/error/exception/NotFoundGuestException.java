package com.example.fashionforecastbackend.global.error.exception;

import com.example.fashionforecastbackend.global.error.ErrorCode;
import com.example.fashionforecastbackend.global.error.GeneralException;

public class NotFoundGuestException extends GeneralException {
	public NotFoundGuestException(ErrorCode errorCode) {
		super(errorCode);
	}
}
