package com.example.fashionforecastbackend.global.error.exception;

import com.example.fashionforecastbackend.global.error.ErrorCode;
import com.example.fashionforecastbackend.global.error.GeneralException;

public class NotFoundBoardException extends GeneralException {
	public NotFoundBoardException(ErrorCode errorCode) {
		super(errorCode);
	}
}
