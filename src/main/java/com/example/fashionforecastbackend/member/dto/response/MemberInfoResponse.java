package com.example.fashionforecastbackend.member.dto.response;

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
	String imageUrl,
	String socialId
) {
	public static MemberInfoResponse of(final Member member) {
		PersonalSetting personalSetting = member.getPersonalSetting();

		return MemberInfoResponse.builder()
			.nickname(member.getNickname())
			.region(personalSetting.getRegion())
			.outingStartTime(personalSetting.getOutingStartTime())
			.outingEndTime(personalSetting.getOutingEndTime())
			.tempCondition(personalSetting.getTempCondition())
			.gender(member.getGender())
			.imageUrl(member.getImageUrl())
			.socialId(member.getSocialId())
			.build();
	}
}
