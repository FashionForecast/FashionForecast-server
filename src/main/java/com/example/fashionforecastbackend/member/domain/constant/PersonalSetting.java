package com.example.fashionforecastbackend.member.domain.constant;

import static com.example.fashionforecastbackend.recommend.domain.TempCondition.*;

import com.example.fashionforecastbackend.recommend.domain.TempCondition;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;

@Getter
@Embeddable
public class PersonalSetting {
	private static final String DEFAULT = "DEFAULT";
	private String region;
	private String outingStartTime;
	private String outingEndTime;
	@Enumerated(EnumType.STRING)
	private TempCondition tempCondition;

	public PersonalSetting() {
		this.region = DEFAULT;
		this.outingStartTime = DEFAULT;
		this.outingEndTime = DEFAULT;
		this.tempCondition = NORMAL;
	}

	public void updateRegion(String region) {
		this.region = region;
	}

	public void updateOutingTime(String startTime, String endTime) {
		this.outingStartTime = startTime;
		this.outingEndTime = endTime;
	}

	public void updateTempCondition(TempCondition tempCondition) {
		this.tempCondition = tempCondition;
	}
}
