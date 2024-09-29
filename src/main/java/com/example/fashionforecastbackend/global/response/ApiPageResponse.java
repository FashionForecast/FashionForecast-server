package com.example.fashionforecastbackend.global.response;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Getter;

@Getter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ApiPageResponse<T> {

	private final List<T> data;
	private final boolean first;
	private final boolean last;
	private final long totalElements;
	private final int pageNumber;
	private final int pageSize;
	private final long offset;
	private final Sort sort;
	private final int code;
	private final String message;

	private ApiPageResponse(Page<T> data, int code, String message) {
		this.data = data.getContent();
		this.first = data.isFirst();
		this.last = data.isLast();
		this.totalElements = data.getTotalElements();
		this.pageNumber = data.getNumber();
		this.pageSize = data.getSize();
		this.offset = data.getPageable().getOffset();
		this.sort = data.getSort();
		this.code = code;
		this.message = message;
	}

	public static <T> ApiPageResponse<T> ok(Page<T> data) {
		return new ApiPageResponse<>(data, 200, "OK");
	}

}
