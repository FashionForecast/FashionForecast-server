package com.example.fashionforecastbackend.global.oauth2;

import java.util.Map;

import com.example.fashionforecastbackend.global.oauth2.userinfo.GoogleOatuh2UserInfo;
import com.example.fashionforecastbackend.global.oauth2.userinfo.KakaoOauth2UserInfo;
import com.example.fashionforecastbackend.global.oauth2.userinfo.Oauth2UserInfo;
import com.example.fashionforecastbackend.member.domain.Member;
import com.example.fashionforecastbackend.member.domain.MemberJoinType;
import com.example.fashionforecastbackend.member.domain.MemberRole;

import lombok.Builder;
import lombok.Getter;

/**
 * 소셜별 데이터를 처리하는 DTO
 */
@Getter
public class Oauth2Attributes {

	private final String userNameAttributeKey;
	private final Oauth2UserInfo oauth2UserInfo;

	@Builder
	public Oauth2Attributes(final String userNameAttributeKey, final Oauth2UserInfo oauth2UserInfo) {
		this.userNameAttributeKey = userNameAttributeKey;
		this.oauth2UserInfo = oauth2UserInfo;
	}

	public static Oauth2Attributes of(final MemberJoinType joinType, final String userNameAttributeKey,
		final Map<String, Object> attributes) {

		if (joinType.equals(MemberJoinType.GOOGLE)) {
			return ofGoogle(userNameAttributeKey, attributes);
		}
		return ofKakao(userNameAttributeKey, attributes);

	}

	private static Oauth2Attributes ofGoogle(final String userNameAttributeKey, final Map<String, Object> attributes) {
		return Oauth2Attributes.builder()
			.userNameAttributeKey(userNameAttributeKey)
			.oauth2UserInfo(new GoogleOatuh2UserInfo(attributes))
			.build();
	}

	private static Oauth2Attributes ofKakao(final String userNameAttributeKey, final Map<String, Object> attributes) {
		return Oauth2Attributes.builder()
			.userNameAttributeKey(userNameAttributeKey)
			.oauth2UserInfo(new KakaoOauth2UserInfo(attributes))
			.build();
	}

	public Member toEntity(MemberJoinType joinType, Oauth2UserInfo oauth2UserInfo) {
		return Member.builder()
			.joinType(joinType)
			.socialId(oauth2UserInfo.getId())
			.nickname(oauth2UserInfo.getNickname())
			.email(oauth2UserInfo.getEmail())
			.role(MemberRole.MEMBER)
			.imageUrl(oauth2UserInfo.getImageUrl())
			.build();

	}
}
