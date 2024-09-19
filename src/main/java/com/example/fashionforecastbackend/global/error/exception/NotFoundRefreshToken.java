package com.example.fashionforecastbackend.global.error.exception;

import com.example.fashionforecastbackend.global.error.ErrorCode;
import com.example.fashionforecastbackend.global.error.GeneralException;

public class NotFoundRefreshToken extends GeneralException {

	public NotFoundRefreshToken(final ErrorCode errorCode) {
		super(errorCode);
	}
}
