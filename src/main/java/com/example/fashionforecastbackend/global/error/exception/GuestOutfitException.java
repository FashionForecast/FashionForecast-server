package com.example.fashionforecastbackend.global.error.exception;

import com.example.fashionforecastbackend.global.error.ErrorCode;
import com.example.fashionforecastbackend.global.error.GeneralException;

public class GuestOutfitException extends GeneralException {
	public GuestOutfitException(ErrorCode errorCode) {
		super(errorCode);
	}
}
