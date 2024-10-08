package com.example.fashionforecastbackend.member.domain.constant;

import java.time.LocalTime;

import com.example.fashionforecastbackend.recommend.domain.TempCondition;
import com.example.fashionforecastbackend.region.domain.Address;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class PersonalSetting {
	private String region;
	private Address address;
	private LocalTime outingStartTime;
	private LocalTime outingEndTime;
	private TempCondition tempCondition;

	public static String formatting(LocalTime time) {
		String amPm = time.getHour() < 12 ? "오전" : "오후";
		int hour = time.getHour() % 12;
		hour = (hour == 0) ? 12 : hour;

		return String.format("%s %02d시", amPm, hour);
	}
}
