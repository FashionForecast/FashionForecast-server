package com.example.fashionforecastbackend.global.error;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.fashionforecastbackend.global.response.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

	// @ExceptionHandler(value = Exception.class)
	// public ResponseEntity<ErrorResponse<Void>> handleException(Exception e) {
	// 	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	// 		.body(ErrorResponse.error("500", e.getMessage()));
	// }
	//
	// @ExceptionHandler(value = RuntimeException.class)
	// public ResponseEntity<ErrorResponse<Void>> handleRuntimeException(RuntimeException e) {
	// 	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	// 		.body(ErrorResponse.error("500", e.getMessage()));
	// }

	/**
	 * 커스텀 예외
	 */
	@ExceptionHandler(value = GeneralException.class)
	public ResponseEntity<ErrorResponse<Void>> handleCustomException(GeneralException e) {
		ErrorCode errorCode = e.getErrorCode();
		return ResponseEntity.status(errorCode.getHttpStatus())
			.body(ErrorResponse.error(errorCode.getCode(), errorCode.getMessage()));
	}

	/**
	 * 데이터 유효성 검사가 실패할 경우
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected ResponseEntity<ErrorResponse<String>> handleMethodArgumentNotValidException(
		MethodArgumentNotValidException e) {
		Map<String, String> errors = new HashMap<>();

		BindingResult bindingResult = e.getBindingResult();
		for (FieldError er : bindingResult.getFieldErrors()) {
			errors.put(er.getField(), er.getDefaultMessage());
		}

		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body(ErrorResponse.error(errors, ErrorCode.INVALID_TYPE_VALUE.getCode(),
				ErrorCode.INVALID_TYPE_VALUE.getMessage()));
	}

	@ExceptionHandler(TypeMismatchException.class)
	public ResponseEntity<ErrorResponse<Void>> handleTypeMismatchException() {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body(ErrorResponse.error("400", "board/invalidId"));
	}

}


