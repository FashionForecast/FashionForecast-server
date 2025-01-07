package com.example.fashionforecastbackend.global.error.exception;

import com.example.fashionforecastbackend.global.error.ErrorCode;
import com.example.fashionforecastbackend.global.error.GeneralException;

public class AlreadyExistException extends GeneralException {
	public AlreadyExistException(final ErrorCode errorCode) {
		super(errorCode);
	}
}
