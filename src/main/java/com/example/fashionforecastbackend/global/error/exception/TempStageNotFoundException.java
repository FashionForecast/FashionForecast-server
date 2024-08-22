package com.example.fashionforecastbackend.global.error.exception;

import com.example.fashionforecastbackend.global.error.ErrorCode;
import com.example.fashionforecastbackend.global.error.GeneralException;

public class TempStageNotFoundException extends GeneralException {
    public TempStageNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
