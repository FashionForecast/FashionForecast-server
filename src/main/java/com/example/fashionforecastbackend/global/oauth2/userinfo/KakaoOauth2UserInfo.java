package com.example.fashionforecastbackend.global.oauth2.userinfo;

import java.util.Map;

public class KakaoOauth2UserInfo implements Oauth2UserInfo {

	private final Map<String, Object> attributes;
	private final Map<String, Object> account;
	private final Map<String, Object> profile;

	public KakaoOauth2UserInfo(final Map<String, Object> attributes) {
		this.attributes = attributes;
		this.account =  (Map<String, Object>) attributes.get("kakao_account");
		this.profile = (Map<String, Object>) account.get("profile");
	}

	@Override
	public String getId() {
		return String.valueOf(attributes.get("id"));
	}

	@Override
	public String getNickname() {

		return getProfileValue("nickname");
	}

	@Override
	public String getImageUrl() {

		return getProfileValue("profile_image_url");
	}

	@Override
	public String getEmail() {
		if (isEmptyAccountOrProfile()) {
			return null;
		}
		return (String) account.get("email");
	}

	private String getProfileValue(String key) {
		if (isEmptyAccountOrProfile()) {
			return null;
		}
		return (String) profile.get(key);
	}

	private boolean isEmptyAccountOrProfile() {
		return account.isEmpty() || profile.isEmpty();
	}
}
