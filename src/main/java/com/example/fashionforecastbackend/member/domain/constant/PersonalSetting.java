package com.example.fashionforecastbackend.member.domain.constant;

import java.time.LocalTime;

import com.example.fashionforecastbackend.recommend.domain.TempCondition;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class PersonalSetting {
	private String region;
	private LocalTime outingStartTime;
	private LocalTime outingEndTime;
	@Enumerated(EnumType.STRING)
	private TempCondition tempCondition;

	public static String formatting(LocalTime time) {
		String amPm = time.getHour() < 12 ? "오전" : "오후";
		int hour = time.getHour() % 12;
		hour = (hour == 0) ? 12 : hour;

		return String.format("%s %02d시", amPm, hour);
	}

	public void updateRegion(String region) {
		this.region = region;
	}

	public void updateOutingTime(LocalTime startTime, LocalTime endTime) {
		this.outingStartTime = startTime;
		this.outingEndTime = endTime;
	}

	public void updateTempCondition(TempCondition tempCondition) {
		this.tempCondition = tempCondition;
	}
}
