package com.example.fashionforecastbackend.global.oauth2.userinfo;

import java.util.Map;

public class GoogleOatuh2UserInfo implements Oauth2UserInfo {

	private final Map<String, Object> attributes;

	public GoogleOatuh2UserInfo(final Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	@Override
	public String getId() {
		return (String) attributes.get("sub");
	}

	@Override
	public String getNickname() {
		return (String) attributes.get("name");
	}

	@Override
	public String getImageUrl() {
		return (String) attributes.get("picture");
	}

	@Override
	public String getEmail() {
		return (String) attributes.get("email");
	}
}
