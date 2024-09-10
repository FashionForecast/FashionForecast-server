package com.example.fashionforecastbackend.global.oauth2.userinfo;

public interface Oauth2UserInfo {

	String getId(); // 구글 : "sub", 카카오 - "id"

	String getNickname();

	String getImageUrl();

	String getEmail();

}
