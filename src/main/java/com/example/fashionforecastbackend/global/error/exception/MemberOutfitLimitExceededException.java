package com.example.fashionforecastbackend.global.error.exception;

import com.example.fashionforecastbackend.global.error.ErrorCode;
import com.example.fashionforecastbackend.global.error.GeneralException;

public class MemberOutfitLimitExceededException extends GeneralException {

	public MemberOutfitLimitExceededException(final ErrorCode errorCode) {
		super(errorCode);
	}
}
