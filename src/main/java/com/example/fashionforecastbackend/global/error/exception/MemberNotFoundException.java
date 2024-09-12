package com.example.fashionforecastbackend.global.error.exception;

import com.example.fashionforecastbackend.global.error.ErrorCode;
import com.example.fashionforecastbackend.global.error.GeneralException;

public class MemberNotFoundException extends GeneralException {

	public MemberNotFoundException(final ErrorCode errorCode) {
		super(errorCode);
	}
}
