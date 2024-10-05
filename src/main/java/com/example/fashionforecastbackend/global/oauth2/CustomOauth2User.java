package com.example.fashionforecastbackend.global.oauth2;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import lombok.Getter;

/**
 * DefaultOAuth2User를 확장해 memberId, email, role 정보를 추가로 담는 클래스.
 * OAuth2 인증 후 사용자 권한과 속성 외에, 이메일과 역할 정보를 추가로 관리.
 */
@Getter
public class CustomOauth2User extends DefaultOAuth2User {

	private final Long memberId;
	private final String email;
	private final String role;
	private final boolean isNewUser;

	/**
	 * CustomOauth2User 생성자.
	 *
	 * @param authorities          유저 권한 목록
	 * @param attributes           OAuth2 제공자에서 받은 사용자 정보
	 * @param userNameAttributeKey OAuth2 제공자가 반환하는 사용자 정보 중 **주요 식별자(PK)**를 지정하기 위해 사용 e.g) Google : "sub", Kakao : "id"
	 * @param email                이메일
	 * @param role                 권한
	 * @param isNewUser            새로운 유저인지 여부
	 */
	public CustomOauth2User(final Collection<? extends GrantedAuthority> authorities,
		final Map<String, Object> attributes, final String userNameAttributeKey, final Long memberId,
		final String email, final String role,
		boolean isNewUser) {
		super(authorities, attributes, userNameAttributeKey);
		this.memberId = memberId;
		this.email = email;
		this.role = role;
		this.isNewUser = isNewUser;
	}
}
