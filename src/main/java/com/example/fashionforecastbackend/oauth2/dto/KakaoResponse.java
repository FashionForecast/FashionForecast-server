package com.example.fashionforecastbackend.oauth2.dto;

import java.util.Map;

public class KakaoResponse implements OAuth2Response {

    private final Map<String, Object> attributes;

    public KakaoResponse(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getEmail() {
        return (String) ((Map<String, Object>) attributes.get("kakao_account")).get("email");
    }

    @Override
    public String getName() {
        return (String) ((Map<String, Object>) attributes.get("properties")).get("nickname");
    }

    @Override
    public String getAge() {
        return (String) ((Map<String, Object>) attributes.get("kakao_account")).get("age_range");
    }

    @Override
    public String getGender() {
        return (String) ((Map<String, Object>) attributes.get("kakao_account")).get("gender");
    }

    @Override
    public String toString() {
        return "KakaoResponse{" +
                "attributes=" + attributes +
                '}';
    }
}
