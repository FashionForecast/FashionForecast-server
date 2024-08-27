package com.example.fashionforecastbackend.global.error.exception;

import com.example.fashionforecastbackend.global.error.ErrorCode;
import com.example.fashionforecastbackend.global.error.GeneralException;

public class OutfitTypeNotFoundException extends GeneralException {
    public OutfitTypeNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
