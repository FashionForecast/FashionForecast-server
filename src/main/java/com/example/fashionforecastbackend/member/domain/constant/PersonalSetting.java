package com.example.fashionforecastbackend.member.domain.constant;

import java.time.LocalTime;

import com.example.fashionforecastbackend.recommend.domain.TempCondition;

import jakarta.persistence.Embeddable;
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
	private TempCondition tempCondition;
}
