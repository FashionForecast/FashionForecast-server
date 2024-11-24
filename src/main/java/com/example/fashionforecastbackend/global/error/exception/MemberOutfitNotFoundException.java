package com.example.fashionforecastbackend.global.error.exception;

import com.example.fashionforecastbackend.global.error.ErrorCode;
import com.example.fashionforecastbackend.global.error.GeneralException;

public class MemberOutfitNotFoundException extends GeneralException {

	public MemberOutfitNotFoundException(final ErrorCode errorCode) {
		super(errorCode);
	}
}
