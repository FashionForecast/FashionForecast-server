package com.example.fashionforecastbackend.member.domain;

public record MemberDeleteEvent(
	Long memberId
) {

	public static MemberDeleteEvent of(final Long memberId) {
		return new MemberDeleteEvent(memberId);
	}
}
