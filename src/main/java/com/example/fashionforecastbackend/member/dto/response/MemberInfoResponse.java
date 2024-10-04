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
	Gender gender
) {
	public static MemberInfoResponse of(Member member) {
		return MemberInfoResponse.builder()
			.nickname(member.getNickname())
			.region(member.getPersonalSetting().getRegion())
			.outingStartTime(PersonalSetting.formatting(member.getPersonalSetting().getOutingStartTime()))
			.outingEndTime(PersonalSetting.formatting(member.getPersonalSetting().getOutingEndTime()))
			.tempCondition(member.getPersonalSetting().getTempCondition())
			.gender(member.getGender())
			.build();
	}
}
