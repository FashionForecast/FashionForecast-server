package com.example.fashionforecastbackend.member.dto.response;

import java.util.Optional;

import com.example.fashionforecastbackend.member.domain.Member;
import com.example.fashionforecastbackend.member.domain.constant.Gender;
import com.example.fashionforecastbackend.member.domain.constant.PersonalSetting;
import com.example.fashionforecastbackend.recommend.domain.TempCondition;

import lombok.Builder;

@Builder
public record MemberInfoResponse(
	String nickname,
	String region,
	String outingStartTime,
	String outingEndTime,
	TempCondition tempCondition,
	Gender gender,
	String imageUrl
) {
	public static MemberInfoResponse of(final Member member) {
		PersonalSetting personalSetting = member.getPersonalSetting();

		return MemberInfoResponse.builder()
			.nickname(member.getNickname())
			.region(Optional.ofNullable(personalSetting)
				.map(PersonalSetting::getRegion)
				.orElse(null))
			.outingStartTime(Optional.ofNullable(personalSetting)
				.map(setting -> PersonalSetting.formatting(setting.getOutingStartTime()))
				.orElse(null))
			.outingEndTime(Optional.ofNullable(personalSetting)
				.map(setting -> PersonalSetting.formatting(setting.getOutingEndTime()))
				.orElse(null))
			.tempCondition(Optional.ofNullable(personalSetting)
				.map(PersonalSetting::getTempCondition)
				.orElse(null))
			.gender(member.getGender())
			.imageUrl(member.getImageUrl())
			.build();
	}
}
