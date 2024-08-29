package com.example.fashionforecastbackend.global.error.exception;

import com.example.fashionforecastbackend.global.error.ErrorCode;
import com.example.fashionforecastbackend.global.error.GeneralException;

public class SearchNotExistException extends GeneralException {

	public SearchNotExistException(ErrorCode errorCode) {
		super(errorCode);
	}
}
