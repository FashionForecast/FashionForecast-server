package com.example.fashionforecastbackend.weather.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeConvertor {

	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

	private LocalDateTimeConvertor() {}

	public static LocalDateTime convert(final String date, final String time) {
		return LocalDateTime.parse(date + time, FORMATTER);
	}
}
